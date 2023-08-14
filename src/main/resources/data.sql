INSERT INTO roles (name) VALUE ('ROLE_ADMIN') ON DUPLICATE KEY UPDATE name = 'ROLE_ADMIN';
INSERT INTO roles (name) VALUE ('ROLE_USER') ON DUPLICATE KEY UPDATE name = 'ROLE_USER';

INSERT INTO users(age, email, first_name, last_name, password) VALUES (25, 'admin@mail.com', 'mr', 'Bond', '$2a$10$HgeTLVyPInNCetytNRhcSerj5scylkv.CoV8oiodUWE2s5n49l9pe') ON DUPLICATE KEY UPDATE age = 25;
INSERT INTO users(age, email, first_name, last_name, password) VALUES (15, 'user@mail.com', 'who', 'dat', '$2a$10$yQVjGFl6J5.gkxil4bYuqeszW16IEZ92ybAOxfDTKT.FKhlV9d5j6') ON DUPLICATE KEY UPDATE age = 15;

INSERT INTO users_roles (role_id, user_id) VALUES (1, 1) ON DUPLICATE KEY UPDATE role_id = 1;
INSERT INTO users_roles (role_id, user_id) VALUES (2, 2) ON DUPLICATE KEY UPDATE role_id = 2;