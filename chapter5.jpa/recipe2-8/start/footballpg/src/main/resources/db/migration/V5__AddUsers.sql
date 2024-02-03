CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255)
);

ALTER TABLE albums ADD COLUMN owner_id INTEGER REFERENCES users(id);
ALTER TABLE cards ADD COLUMN owner_id INTEGER REFERENCES users(id);
ALTER TABLE cards ADD CONSTRAINT cards_album_player_key UNIQUE (album_id, player_id);