package com.packt.football.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchEventRepository extends JpaRepository<MatchEventEntity, Long> {

    @Query(nativeQuery = true, value = "SELECT me.* FROM match_events me  WHERE me.match_id = ?1 AND CAST(me.details -> 'type' as INT) = ?2")
    public List<MatchEventEntity> findByIdIncludeEventsOfType(Integer matchId, Integer eventType);

    @Query(nativeQuery = true, value = "SELECT me.id, me.match_id, me.event_time, me.details " +
            "FROM match_events me CROSS JOIN LATERAL jsonb_array_elements(me.details->'players') AS player_id " +
            "WHERE me.match_id = ?1 AND CAST(player_id as INT) = ?2")
    List<MatchEventEntity> findByMatchIdAndPlayer(Integer matchId, Integer playerId);

    /*
     * Following query is expected to fail, just for demonstration purposes
     */
    @Query(nativeQuery = true, value = "SELECT me.id as event_id, me.match_id, me.event_time, me.details " +
            "FROM match_events me CROSS JOIN LATERAL jsonb_array_elements(me.details->'players') AS player_id " +
            "WHERE me.match_id = ?1 AND CAST(player_id as INT) = ?2")
    List<MatchEventEntity> findByMatchIdAndPlayerError(Integer matchId, Integer playerId);
}
