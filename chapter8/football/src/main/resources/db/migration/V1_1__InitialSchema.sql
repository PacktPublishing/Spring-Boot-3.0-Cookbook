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
