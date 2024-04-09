package com.packt.football.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Integer> {
    List<PlayerEntity> findByDateOfBirth(LocalDate dateOfBirth);

    List<PlayerEntity> findByNameContaining(String name);

    @Query("SELECT p FROM PlayerEntity p WHERE p.id IN (?1)")
    List<PlayerEntity> findListOfPlayers(List<Integer> players);

    List<PlayerEntity> findByIdIn(List<Integer> players);

    List<PlayerEntity> findByTeamId(Integer teamId, Sort sort);

    List<PlayerEntity> findByNameStartingWith(String name);

    List<PlayerEntity> findByNameLike(String name);

    @Procedure("FIND_PLAYERS_WITH_MORE_THAN_N_MATCHES")
    Integer getTotalPlayersWithMoreThanNMatches(int num_matches);

    @Query("SELECT p FROM PlayerEntity p JOIN FETCH p.team WHERE p.id = ?1")
    Optional<PlayerEntity> findByIdWithTeam(Integer teamId);
}
