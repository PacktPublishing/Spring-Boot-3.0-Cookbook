CREATE TABLE matches(
    id SERIAL PRIMARY KEY,
    match_date DATE,
    team1_id INT NOT NULL REFERENCES teams(id),
    team2_id INT NOT NULL REFERENCES teams(id),
    team1_goals INT default 0,
    team2_goals INT default 0
);

ALTER TABLE players ADD COLUMN height INT;
ALTER TABLE players ADD COLUMN weight INT;

-- Update all players with an initial value for height and weight:
UPDATE players SET height = 175, weight = 70;

