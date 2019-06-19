INSERT INTO `ROLES`
(`name`)
VALUES
('USER'), ('MODERATOR'), ('ADMIN');

INSERT INTO `USERS`
(`bio`, `email`, `creation_date`, `firstname`, `password`, `username`)
VALUES
('Boring biography', 'admin@photogram.com', CURRENT_TIMESTAMP, 'Alex',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'admin'),

('Photograph', 'photograph@photogram.com', CURRENT_TIMESTAMP, 'Andrzej',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'photograph'),

('Developer', 'developer@photogram.com', CURRENT_TIMESTAMP, 'Heniek',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'heniu666'),

('Gamer', 'gamer@photogram.com', CURRENT_TIMESTAMP, 'Mietek',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'gammer1337'),

('Unemployed', 'unemployed@photogram.com', CURRENT_TIMESTAMP, 'Zbyszek',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'poorMan');

INSERT INTO `CONFIRMATIONS`
(`user_id`, `confirmed`, `creation_date`, `token`)
VALUES
(1, true, CURRENT_TIMESTAMP , 'afd0b036625a3aa8b6399dc8c8fff0ff'),
(2, true, CURRENT_TIMESTAMP , '9c45c2f117613daaad311ff8703ae846'),
(3, true, CURRENT_TIMESTAMP , '15e0ba0710e43d7faaffc00fed873c88'),
(4, false, CURRENT_TIMESTAMP , 'bc27b4dbbc0f34f9ae8e4b72f2d51b60');

INSERT INTO `USERS_ROLES`
(`user_id`, `role_id`)
VALUES
(1, 1), (1, 2), (1, 3), (2, 1), (2, 2), (3, 1), (4, 1), (5, 1);


INSERT INTO `POSTS`
(`caption`, `likes`, `dislikes`, `location`, `creation_date`, `user_id`, `valid`, `visibility`)
VALUES
('First post, and the future prediction for this project', 666, 1, 'Wrocław', CURRENT_TIMESTAMP , 1, true, 'PUBLIC'),
('Warsaw from Heaven', 123, 2, 'Warszawa', CURRENT_TIMESTAMP , 1, true, 'PRIVATE'),
('Just an cat', 1, 3, 'Wrocław', CURRENT_TIMESTAMP , 1, true, 'FOLLOWERS'),
('Wroclaw from Heaven', 13, 4, 'Warszawa', CURRENT_TIMESTAMP , 2, true, 'PUBLIC'),
('Just an dog', 12, 5, 'Wrocław', CURRENT_TIMESTAMP , 2, true, 'PUBLIC'),
('Gdansk from Heaven', 4, 6, 'Gdansk', CURRENT_TIMESTAMP , 3, true, 'PUBLIC'),
('Just an birth', 11, 7, 'Wrocław', CURRENT_TIMESTAMP , 3, true, 'PUBLIC');

INSERT INTO `IMAGES`
(`image_id`, `extension`)
VALUES
(1000, 'jpg');

INSERT INTO `POST_IMAGES`
(`post_id`, `image_id`)
VALUES
(1, 1);

INSERT INTO `COMMENTS` (`content`, `creation_date`, `post_id`, `user_id`)
VALUES ('First comment ever', CURRENT_TIMESTAMP , 1, 1);