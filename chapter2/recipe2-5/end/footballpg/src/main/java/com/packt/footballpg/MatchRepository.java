package com.packt.footballpg;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchRepository extends JpaRepository<MatchEntity, Integer> {
        @Query("SELECT p1 FROM MatchEntity m JOIN m.team1 t1 JOIN t1.players p1 WHERE m.id = ?1 UNION SELECT p2 FROM MatchEntity m JOIN m.team2 t2 JOIN t2.players p2 WHERE m.id = ?1")
        public List<PlayerEntity> findPlayersByMatchId(Integer matchId);

        @Query("SELECT m FROM MatchEntity m JOIN FETCH m.events e JOIN FETCH m.team1 JOIN FETCH m.team2 WHERE m.id = ?1")
        public Optional<MatchEntity> findByIdWithTimeline(Integer matchId);

        @Query(nativeQuery = true, value = "SELECT me.id, me.match_id, me.event_time,me.details " +
                        "FROM match_events me " +
                        "CROSS JOIN LATERAL jsonb_array_elements(me.details->'players') AS player_id where me.match_id = ?1 and CAST(player_id as INT) = ?2")
        public List<MatchEventEntity> findPlayerTimeline(Integer matchId, Integer playerId);

        // @Query(nativeQuery = true, value = "SELECT\r\n" + //
        // "\tm1_0.id,m1_0.match_date,m1_0.team1_id,t1_0.name,m1_0.team1_goals,m1_0.team2_id,t2_0.name,m1_0.team2_goals,\r\n"
        // + //
        // // " me.id,\r\n" + //
        // " me.match_id,\r\n" + //
        // " me.event_time,\r\n" + //
        // "\tme.details\r\n" + //
        // "FROM match_events me\r\n" + //
        // "CROSS JOIN LATERAL jsonb_array_elements(me.details->'players') AS
        // player_id\r\n" + //
        // "JOIN matches m1_0 ON me.match_id = m1_0.id\r\n" + //
        // "join teams t1_0 on t1_0.id=m1_0.team1_id \r\n" + //
        // "join teams t2_0 on t2_0.id=m1_0.team2_id\r\n" + //
        // "where me.match_id = 400222852 and CAST(player_id as INT) = 467658")
        // public Optional<MatchEntity> findByIdWithPlayerTimeline(Integer matchId,
        // Integer playerId);

        @Query(nativeQuery = true, value = "SELECT me.* FROM match_events me  WHERE me.match_id = ?1 AND me.details -> 'type' = ?2")
        public List<MatchEventEntity> findByIdIncludeEventsOfType(Integer matchId,
                        @Param(value = "eventType") Integer eventType);

        // @Query(value = "SELECT me FROM match_events me WHERE me.match.id = ?1 AND
        // me.details.type = ?2")
        // public List<MatchEventEntity> findByIdIncludeEventsOfTypeJp(Integer matchId,
        // Integer eventType);

        @Query(value = "SELECT * FROM match_events WHERE details->'players' @> ?1::jsonb", nativeQuery = true)
        List<MatchEventEntity> findByPlayerId(Integer playerId);

}
