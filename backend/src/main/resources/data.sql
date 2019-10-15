INSERT INTO `ROLES`
(`name`)
VALUES
('USER'), ('MODERATOR'), ('ADMIN');

INSERT INTO `USERS`
(`bio`, `email`, `creation_date`, `firstname`, `password`, `username`)
VALUES
('Software developer', 'admin@photogram.com', CURRENT_TIMESTAMP, 'Alex',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'admin'),

('Professional photographer', 'peter@photogram.com', CURRENT_TIMESTAMP, 'Peter',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'photograph'),

('500+ receiver', 'andrew@photogram.com', CURRENT_TIMESTAMP, 'Andrew',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'andreW'),

('Model', 'charlotte@photogram.com', CURRENT_TIMESTAMP, 'Charlotte',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'baker'),

('Your nightmare', 'ava@photogram.com', CURRENT_TIMESTAMP, 'Ava',
'$2a$10$Q2YP0GJki3D/.yKIqFs54.aHnVhpuLvHB.YftUaiQ3733lWZnwZea', 'ava');

INSERT INTO `CONFIRMATIONS`
(`user_id`, `confirmed`, `creation_date`, `token`)
VALUES
(1, true, CURRENT_TIMESTAMP , 'afd0b036625a3aa8b6399dc8c8fff0ff'),
(2, true, CURRENT_TIMESTAMP , '9c45c2f117613daaad311ff8703ae846'),
(3, true, CURRENT_TIMESTAMP , '15e0ba0710e43d7faaffc00fed873c88'),
(4, false, CURRENT_TIMESTAMP , 'bc27b4dbbc0f34f9ae8e4b72f2d51b60'),
(5, true, CURRENT_TIMESTAMP , 'bc27b4dbbc0f34f9ae8e4b72f2d51b66');

INSERT INTO `USERS_ROLES`
(`user_id`, `role_id`)
VALUES
(1, 1), (1, 2), (1, 3), (2, 1), (2, 2), (3, 1), (4, 1), (5, 1);


INSERT INTO `POSTS`
(`caption`, `likes`, `dislikes`, `location`, `creation_date`, `user_id`, `valid`, `visibility`)
VALUES
('Found at photograms.org', 666, 1, 'Wrocław', CURRENT_TIMESTAMP , 1, true, 'PUBLIC'),
('Found at photograms.org', 123, 2, 'Warszawa', CURRENT_TIMESTAMP , 1, true, 'PRIVATE'),
('Found at photograms.org', 1, 3, 'Olesno', CURRENT_TIMESTAMP , 1, true, 'FOLLOWERS'),
('Found at photograms.org', 13, 4, 'Gdańsk', CURRENT_TIMESTAMP , 2, true, 'PUBLIC'),
('Found at photograms.org', 12, 5, 'Kluczbork', CURRENT_TIMESTAMP , 2, true, 'PUBLIC'),
('Found at photograms.org', 4, 6, 'Opole', CURRENT_TIMESTAMP , 3, true, 'PUBLIC'),
('Just an birth', 11, 7, 'Wrocław', '2019-06-24 13:42:09.446186', 3, false, 'PUBLIC');

INSERT INTO `IMAGES`
(`image_id`, `extension`)
VALUES
(1000, 'jpg'),
(1000, 'jpg'),
(1000, 'jpg'),
(1000, 'jpg'),
(1000, 'jpg'),
(1000, 'jpg');

INSERT INTO `POST_IMAGES`
(`post_id`, `image_id`)
VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6);

INSERT INTO `COMMENTS` (`content`, `creation_date`, `post_id`, `user_id`)
VALUES ('First comment ever', CURRENT_TIMESTAMP , 1, 1);