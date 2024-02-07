package com.packt.football.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchRepository extends JpaRepository<MatchEntity, Integer> {
        @Query("SELECT p1 FROM MatchEntity m JOIN m.team1 t1 JOIN t1.players p1 WHERE m.id = ?1 UNION SELECT p2 FROM MatchEntity m JOIN m.team2 t2 JOIN t2.players p2 WHERE m.id = ?1")
        public List<PlayerEntity> findPlayersByMatchId(Integer matchId);

        @Query("SELECT m FROM MatchEntity m JOIN FETCH m.events e JOIN FETCH m.team1 JOIN FETCH m.team2 WHERE m.id = ?1")
        public Optional<MatchEntity> findByIdWithTimeline(Integer matchId);

        // @Query(nativeQuery = true, value = "SELECT me.id, me.match_id, me.event_time, me.details FROM match_events me CROSS JOIN LATERAL jsonb_array_elements(me.details->'players') AS player_id WHERE me.match_id = ?1 AND CAST(player_id as INT) = ?2")
        // List<MatchEventEntity> findPlayerTimeline(Integer matchId, Integer playerId);
}
