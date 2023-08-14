CREATE TABLE IF NOT EXISTS `users` (
                                       user_id int(11) NOT NULL AUTO_INCREMENT,
                                       email varchar(50) NOT NULL UNIQUE ,
                                       password varchar(100) NOT NULL,
                                       first_name varchar(50) DEFAULT NULL,
                                       last_name varchar(50) DEFAULT NULL,
                                       age int(3) DEFAULT NULL,
                                       PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `roles` (
                                       role_id int(11) NOT NULL AUTO_INCREMENT,
                                       name varchar(50) DEFAULT NULL UNIQUE,
                                       PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `users_roles` (
                                             user_id int(11) NOT NULL,
                                             role_id int(11) NOT NULL,
                                             PRIMARY KEY (`user_id`,`role_id`),
                                             KEY `user_id` (`user_id`),
                                             CONSTRAINT `users_roles_ibfk_1`
                                                 FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                                             CONSTRAINT `users_roles_ibfk_2`
                                                 FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;