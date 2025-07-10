CREATE SCHEMA game;
CREATE SCHEMA liquidbase;

CREATE ROLE game_user_role;
GRANT game_user_role TO game_user;

GRANT ALL PRIVILEGES ON SCHEMA game TO game_user_role;
GRANT ALL PRIVILEGES ON SCHEMA liquidbase TO game_user_role