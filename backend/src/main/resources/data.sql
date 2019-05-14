INSERT INTO `ROLE` (`name`) VALUES ('USER');
INSERT INTO `ROLE` (`name`) VALUES ('MODERATOR');
INSERT INTO `ROLE` (`name`) VALUES ('ADMIN');

INSERT INTO `USER` (`bio`, `email`, `email_confirmed`, `creation_date`, `name`, `password`, `username`)
VALUES ('Boring biography', 'admin@photogram.com', true, CURRENT_TIMESTAMP, 'Alex', '$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'admin');

INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (1, 1);
INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (1, 2);
INSERT INTO `USERS_ROLES` (`user_id`, `role_id`) VALUES  (1, 3);