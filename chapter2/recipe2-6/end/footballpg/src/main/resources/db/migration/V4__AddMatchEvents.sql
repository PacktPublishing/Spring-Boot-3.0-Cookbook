CREATE TABLE match_events (
    id BIGSERIAL PRIMARY KEY,
    match_id INTEGER NOT NULL,
    event_time TIMESTAMP NOT NULL,
    details JSONB,
    FOREIGN KEY (match_id) REFERENCES matches (id)
);

CREATE PROCEDURE FIND_PLAYERS_WITH_MORE_THAN_N_MATCHES(IN num_matches INT, OUT count_out INT)
LANGUAGE plpgsql
AS $$
BEGIN 
    WITH PLAYERS_WITH_MATCHES AS 
        (SELECT p.id, count(m.id) AS match_count FROM players p, matches m WHERE p.team_id = m.team1_id OR p.team_id = m.team2_id
        GROUP BY p.id HAVING count(m.id) > num_matches)
    SELECT COUNT(1) INTO count_out FROM PLAYERS_WITH_MATCHES;
END;
$$;

