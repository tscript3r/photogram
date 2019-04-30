INSERT INTO `ROLE` (`name`) VALUES ('ADMIN');
INSERT INTO `ROLE` (`name`) VALUES ('MODERATOR');
INSERT INTO `ROLE` (`name`) VALUES ('USER');

INSERT INTO `USER` (`bio`, `email`, `email_confirmed`, `creation_date`, `name`, `password`, `username`)
VALUES ('Boring biography', 'admin@photogram.com', true, CURRENT_TIMESTAMP, 'Alex', '$2a$10$zVQjiekD/ent/AGDmC29LONOJ.HmXBJ6v/RFmAxCMgk8M.wUsmvMO', 'admin');

INSERT INTO `USER_ROLES` (`user_id`, `roles_id`) VALUES  (1, 1);
INSERT INTO `USER_ROLES` (`user_id`, `roles_id`) VALUES  (1, 2);
INSERT INTO `USER_ROLES` (`user_id`, `roles_id`) VALUES  (1, 3);