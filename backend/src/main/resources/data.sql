INSERT INTO `ROLES`
(`name`)
VALUES
('USER'), ('MODERATOR'), ('ADMIN');

INSERT INTO `USERS`
(`bio`, `email`, `email_confirmed`, `creation_date`, `firstname`, `password`, `username`)
VALUES
('Boring biography', 'admin@photogram.com', true, CURRENT_TIMESTAMP, 'Alex',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'admin'),

('Photograph', 'photograph@photogram.com', true, CURRENT_TIMESTAMP, 'Andrzej',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'photograph'),

('Developer', 'developer@photogram.com', true, CURRENT_TIMESTAMP, 'Heniek',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'heniu666'),

('Gamer', 'gamer@photogram.com', true, CURRENT_TIMESTAMP, 'Mietek',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'gammer1337'),

('Unemployed', 'unemployed@photogram.com', true, CURRENT_TIMESTAMP, 'Zbyszek',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'poorMan');

INSERT INTO `USERS_ROLES`
(`user_id`, `role_id`)
VALUES
(1, 1), (1, 2), (1, 3), (2, 1), (2, 2), (3, 1), (4, 1), (5, 1);


INSERT INTO `POSTS`
(`caption`, `likes`, `dislikes`, `location`, `creation_date`, `user_id`, `valid`)
VALUES
('First post ever', 666, 1, 'Wrocław', CURRENT_TIMESTAMP , 1, true),
('Warsaw from Heaven', 123, 2, 'Warszawa', CURRENT_TIMESTAMP , 1, true),
('Just an cat', 1, 3, 'Wrocław', CURRENT_TIMESTAMP , 1, true),
('Wroclaw from Heaven', 13, 4, 'Warszawa', CURRENT_TIMESTAMP , 2, true),
('Just an dog', 12, 5, 'Wrocław', CURRENT_TIMESTAMP , 2, true),
('Gdansk from Heaven', 4, 6, 'Gdansk', CURRENT_TIMESTAMP , 3, true),
('Just an birth', 11, 7, 'Wrocław', CURRENT_TIMESTAMP , 3, true);

INSERT INTO `IMAGES`
(`image_id`)
VALUES
(1001), (1002), (1003);

INSERT INTO `POST_IMAGES`
(`post_id`, `image_id`)
VALUES
(1, 1), (1, 2), (1, 3);

INSERT INTO `COMMENTS` (`content`, `creation_date`, `post_id`, `user_id`)
VALUES ('First comment ever', CURRENT_TIMESTAMP , 1, 1);