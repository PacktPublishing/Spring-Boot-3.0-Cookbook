CREATE TABLE albums (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    expire_date DATE
);

CREATE TABLE cards (
    id SERIAL PRIMARY KEY,
    album_id INTEGER REFERENCES albums(id),
    player_id INTEGER REFERENCES players(id)
);

INSERT INTO albums(title, expire_date)
VALUES
('WWC FIFA World Cup 2023. Australia and New Zealand', '2024-12-31');

INSERT INTO cards(album_id, player_id)
SELECT 1 AS album_id, id as player_id FROM players limit 100;