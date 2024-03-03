CREATE TABLE teams (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    jersey_number INT,
    name VARCHAR(255),
    position VARCHAR(255),
    date_of_birth DATE,
    team_id INT REFERENCES teams(id),
    height INT,
    weight INT
);

CREATE TABLE matches(
    id SERIAL PRIMARY KEY,
    match_date DATE,
    team1_id INT NOT NULL REFERENCES teams(id),
    team2_id INT NOT NULL REFERENCES teams(id),
    team1_goals INT default 0,
    team2_goals INT default 0
);



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

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255)
);

CREATE TABLE albums (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    expire_date DATE,
    owner_id INTEGER REFERENCES users(id)
);

CREATE INDEX idx_albums_on_owner_id ON albums(owner_id);

CREATE TABLE cards (
    id SERIAL PRIMARY KEY,
    album_id INTEGER REFERENCES albums(id),
    player_id INTEGER REFERENCES players(id),
    owner_id INTEGER REFERENCES users(id),
    UNIQUE (album_id, player_id)
);

CREATE INDEX idx_cards_on_owner_id ON cards(owner_id);
CREATE INDEX idx_cards_on_album_id ON cards(album_id);
CREATE INDEX idx_cards_on_player_id ON cards(player_id);
