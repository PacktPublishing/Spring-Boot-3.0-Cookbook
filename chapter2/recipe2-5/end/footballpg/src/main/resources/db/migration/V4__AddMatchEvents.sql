CREATE TABLE match_events (
    id BIGSERIAL PRIMARY KEY,
    match_id INTEGER NOT NULL,
    event_time TIMESTAMP NOT NULL,
    details JSONB,
    FOREIGN KEY (match_id) REFERENCES matches (id)
);
