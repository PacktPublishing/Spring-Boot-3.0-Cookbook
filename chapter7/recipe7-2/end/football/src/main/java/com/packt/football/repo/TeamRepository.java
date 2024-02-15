package com.packt.football.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<TeamEntity, Integer> {
    @Query("SELECT t FROM TeamEntity t JOIN FETCH t.players WHERE t.id = ?1")
    public Optional<TeamEntity> findByIdWithPlayers(Integer id);

    @Query("SELECT p.team.name as name, count(p.id) as playersCount FROM PlayerEntity p WHERE p.position = ?1 GROUP BY p.team ORDER BY playersCount DESC")
    public List<TeamPlayers> getNumberOfPlayersByPosition(String position);
}
