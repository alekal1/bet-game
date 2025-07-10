CREATE TABLE game.players
(
    id                  bigserial   NOT NULL,
    username            text        NOT NULL,
    total_amount        numeric     DEFAULT 100,
    CONSTRAINT player_pkey PRIMARY KEY (id)
);

ALTER TABLE game.players
OWNER TO game_user;

GRANT ALL PRIVILEGES ON TABLE game.players to game_user_role;