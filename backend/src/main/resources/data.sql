INSERT INTO `ROLE` (`name`) VALUES ('USER');
INSERT INTO `ROLE` (`name`) VALUES ('MODERATOR');
INSERT INTO `ROLE` (`name`) VALUES ('ADMIN');

INSERT INTO `USER` (`bio`, `email`, `email_confirmed`, `creation_date`, `firstname`, `password`, `username`)
VALUES ('Boring biography', 'admin@photogram.com', true, CURRENT_TIMESTAMP, 'Alex', '$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'admin');

INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (1, 1);
INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (1, 2);
INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (1, 3);

INSERT INTO `POST` (`caption`, `likes`, `location`, `creation_date`, `image_id`, `user_id`)
VALUES ('First post ever', 666, 'Wrocław', CURRENT_TIMESTAMP , 1, 1);

INSERT INTO `COMMENT` (`content`, `creation_date`, `post_id`, `user_id`)
VALUES ('First comment ever', CURRENT_TIMESTAMP , 1, 1);