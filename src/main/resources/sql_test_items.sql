INSERT INTO games (name, description) VALUES
('Super Mario', 'A classic platformer game'),
('The Legend of Zelda', 'An epic adventure game'),
('Minecraft', 'A sandbox game about placing blocks'),
('Fortnite', 'A battle royale game'),
('Among Us', 'A multiplayer party game');

INSERT INTO genres (name) VALUES
('Platformer'),
('Adventure'),
('Sandbox'),
('Battle Royale'),
('Party');


INSERT INTO user_games (user_id, game_id) VALUES
 (1, 1), -- alice plays Super Mario
 (1, 2), -- alice plays The Legend of Zelda
 (1, 3), -- bob plays Minecraft
 (2, 4), -- charlie plays Fortnite
 (2, 5), -- david plays Among Us
 (2, 1), -- eva plays Super Mario
 (2, 3); -- eva plays Minecraft

INSERT INTO game_genre (game_id, genre_id) VALUES
 (1, 1), -- Super Mario is a Platformer
 (2, 2), -- The Legend of Zelda is an Adventure
 (3, 3), -- Minecraft is a Sandbox
 (4, 4), -- Fortnite is a Battle Royale
 (5, 5), -- Among Us is a Party game
 (1, 2), -- Super Mario also has Adventure elements
 (2, 5); -- The Legend of Zelda also has Party elements