INSERT INTO `ROLES` (`name`) VALUES ('USER');
INSERT INTO `ROLES` (`name`) VALUES ('MODERATOR');
INSERT INTO `ROLES` (`name`) VALUES ('ADMIN');

INSERT INTO `USERS` (`bio`, `email`, `email_confirmed`, `creation_date`, `firstname`, `password`, `username`)
VALUES ('Boring biography', 'admin@photogram.com', true, CURRENT_TIMESTAMP, 'Alex',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'admin');
INSERT INTO `USERS` (`bio`, `email`, `email_confirmed`, `creation_date`, `firstname`, `password`, `username`)
VALUES ('Photograph', 'photograph@photogram.com', true, CURRENT_TIMESTAMP, 'Andrzej',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'photograph');
INSERT INTO `USERS` (`bio`, `email`, `email_confirmed`, `creation_date`, `firstname`, `password`, `username`)
VALUES ('Developer', 'developer@photogram.com', true, CURRENT_TIMESTAMP, 'Heniek',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'heniu666');
INSERT INTO `USERS` (`bio`, `email`, `email_confirmed`, `creation_date`, `firstname`, `password`, `username`)
VALUES ('Gamer', 'gamer@photogram.com', true, CURRENT_TIMESTAMP, 'Mietek',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'gammer1337');
INSERT INTO `USERS` (`bio`, `email`, `email_confirmed`, `creation_date`, `firstname`, `password`, `username`)
VALUES ('Unemployed', 'unemployed@photogram.com', true, CURRENT_TIMESTAMP, 'Zbyszek',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'poorMan');

INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (1, 1);
INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (1, 2);
INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (1, 3);
INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (2, 1);
INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (2, 2);
INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (3, 1);
INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (4, 1);
INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (5, 1);


INSERT INTO `POSTS` (`caption`, `likes`, `dislikes`, `location`, `creation_date`, `image_id`, `user_id`, `valid`)
VALUES ('First post ever', 666, 1, 'Wrocław', CURRENT_TIMESTAMP , 1, 1, true);
INSERT INTO `POSTS` (`caption`, `likes`, `dislikes`,  `location`, `creation_date`, `image_id`, `user_id`, `valid`)
VALUES ('Warsaw from Heaven', 123, 2, 'Warszawa', CURRENT_TIMESTAMP , 2, 1, true);
INSERT INTO `POSTS` (`caption`, `likes`, `dislikes`,  `location`, `creation_date`, `image_id`, `user_id`, `valid`)
VALUES ('Just an cat', 1, 3, 'Wrocław', CURRENT_TIMESTAMP , 3, 1, true);

INSERT INTO `POSTS` (`caption`, `likes`, `dislikes`,  `location`, `creation_date`, `image_id`, `user_id`, `valid`)
VALUES ('Wroclaw from Heaven', 13, 4, 'Warszawa', CURRENT_TIMESTAMP , 1, 2, true);
INSERT INTO `POSTS` (`caption`, `likes`, `dislikes`,  `location`, `creation_date`, `image_id`, `user_id`, `valid`)
VALUES ('Just an dog', 12, 5, 'Wrocław', CURRENT_TIMESTAMP , 3, 2, true);

INSERT INTO `POSTS` (`caption`, `likes`, `dislikes`,  `location`, `creation_date`, `image_id`, `user_id`, `valid`)
VALUES ('Gdansk from Heaven', 4, 6, 'Gdansk', CURRENT_TIMESTAMP , 1, 3, true);
INSERT INTO `POSTS` (`caption`, `likes`, `dislikes`,  `location`, `creation_date`, `image_id`, `user_id`, `valid`)
VALUES ('Just an birth', 11, 7, 'Wrocław', CURRENT_TIMESTAMP , 2, 3, true);

INSERT INTO `COMMENTS` (`content`, `creation_date`, `post_id`, `user_id`)
VALUES ('First comment ever', CURRENT_TIMESTAMP , 1, 1);