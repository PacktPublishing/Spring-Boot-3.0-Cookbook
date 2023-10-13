package com.packt.footballpg;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchEventRepository extends JpaRepository<MatchEventEntity, Long> {

    //CROSS JOIN LATERAL jsonb_array_elements(me.details->'players') AS player_id 
    @Query(nativeQuery = true, value = "SELECT me.id, me.match_id, me.event_time, me.details FROM match_events me CROSS JOIN LATERAL jsonb_array_elements(me.details->'players') AS player_id WHERE me.match_id = ?1 AND CAST(player_id as INT) = ?2")
    List<MatchEventEntity> findByIdNative(Integer matchId, Integer playerId);
}
