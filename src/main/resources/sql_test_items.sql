USE gamelibrary;

INSERT INTO games (name, description) VALUES
('Super Mario', 'A classic platformer game'),
('The Legend of Zelda', 'An epic adventure game'),
('Minecraft', 'A sandbox game about placing blocks'),
('Fortnite', 'A battle royale game'),
('Among Us', 'A multiplayer party game');
('Super Mario Odyssey', 'A 3D platformer adventure game featuring Mario'),
('Super Mario Kart', 'A racing game featuring characters from the Mario series'),
('The Legend of Zelda: Breath of the Wild', 'An open-world adventure game set in Hyrule'),
('The Legend of Zelda: Link\'s Awakening', 'A top-down action-adventure game'),
('Super Mario Galaxy', 'A space-themed platformer adventure game');

INSERT INTO genres (name) VALUES
('Platformer'),
('Adventure'),
('Sandbox'),
('Battle Royale'),
('Party');
('Racing'),
('Open World');


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
 (6, 1), -- Super Mario Odyssey is a Platformer
 (6, 2), -- Super Mario Odyssey is also an Adventure
 (7, 6), -- Super Mario Kart is a Racing game
 (8, 2), -- The Legend of Zelda: Breath of the Wild is an Adventure
 (8, 7), -- The Legend of Zelda: Breath of the Wild is also an Open World game
 (9, 2), -- The Legend of Zelda: Link s Awakening is an Adventure
 (10, 1), -- Super Mario Galaxy is a Platformer
 (10, 2); -- Super Mario Galaxy is also an Adventure