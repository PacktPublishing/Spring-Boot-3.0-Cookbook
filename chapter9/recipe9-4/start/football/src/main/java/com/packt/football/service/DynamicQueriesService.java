package com.packt.football.service;



import com.packt.football.domain.Player;
import com.packt.football.mapper.PlayerMapper;

import com.packt.football.repo.PlayerEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DynamicQueriesService {

    private final EntityManager em;
    private final PlayerMapper playerMapper;

    public DynamicQueriesService(EntityManagerFactory emFactory, PlayerMapper playerMapper) {
        this.em = emFactory.createEntityManager();
        this.playerMapper = playerMapper;
    }

    public List<PlayerEntity> searchTeamPlayers(Integer teamId, Optional<String> name, Optional<Integer> minHeight,
                                                Optional<Integer> maxHeight,
                                                Optional<Integer> minWeight, Optional<Integer> maxWeight) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PlayerEntity> cq = cb.createQuery(PlayerEntity.class);
        Root<PlayerEntity> player = cq.from(PlayerEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(player.get("team").get("id"), teamId));
        if (name.isPresent()) {
            predicates.add(cb.like(player.get("name"), "%" + name.get() + "%"));
        }
        if (minHeight.isPresent()) {
            predicates.add(cb.ge(player.get("height"), minHeight.get()));
        }
        if (maxHeight.isPresent()) {
            predicates.add(cb.le(player.get("height"), maxHeight.get()));
        }
        if (minWeight.isPresent()) {
            predicates.add(cb.ge(player.get("weight"), minWeight.get()));
        }
        if (maxWeight.isPresent()) {
            predicates.add(cb.le(player.get("weight"), maxWeight.get()));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<PlayerEntity> query = em.createQuery(cq);
        return query.getResultList();
    }

    public List<Player> searchTeamPlayersAndMap(Integer teamId, Optional<String> name, Optional<Integer> minHeight,
                                                Optional<Integer> maxHeight,
                                                Optional<Integer> minWeight, Optional<Integer> maxWeight) {
        return searchTeamPlayers(teamId, name, minHeight, maxHeight, minWeight, maxWeight)
                .stream()
                .map(p -> playerMapper.map(p))
                .collect(Collectors.toList());
    }

    

    public void deleteEventRange(Integer matchId, LocalDateTime start, LocalDateTime end) {
        try {
            em.clear();
            em.getTransaction().begin();
            Query query = em.createQuery(
                    "DELETE FROM MatchEventEntity e WHERE e.match.id=:matchId AND e.time BETWEEN :start AND :end");
            query.setParameter("matchId", matchId);
            query.setParameter("start", start);
            query.setParameter("end", end);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public List<PlayerEntity> searchUserMissingPlayers(Integer userId) {
        Query query = em.createNativeQuery(
                "SELECT p1.* FROM players p1 WHERE p1.id NOT IN (SELECT c1.player_id FROM cards c1 WHERE c1.owner_id=?1)",
                PlayerEntity.class);
        query.setParameter(1, userId);
        return query.getResultList();
    }

    public List<Player> searchUserMissingPlayersAndMap(Integer userId) {
        return searchUserMissingPlayers(userId)
                .stream()
                .map(playerMapper::map)
                .collect(Collectors.toList());
    }

    public Integer countPlayers() {
        Query query = em.createQuery("SELECT COUNT(p) FROM PlayerEntity p");
        return ((Number) query.getSingleResult()).intValue();
    }

    public Player findPlayerById(Integer id) {
        Query query = em.createQuery("SELECT p FROM PlayerEntity p WHERE p.id=?0", PlayerEntity.class);
        query.setParameter(0, id);
        return playerMapper.map((PlayerEntity) query.getSingleResult());
    }
}
