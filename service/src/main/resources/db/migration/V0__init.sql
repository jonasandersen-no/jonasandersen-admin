CREATE SEQUENCE IF NOT EXISTS permitted_users_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS user_settings_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE permitted_users
(
    id      BIGINT NOT NULL,
    subject VARCHAR(255),
    email   VARCHAR(255),
    CONSTRAINT pk_permitted_users PRIMARY KEY (id)
);

CREATE TABLE user_settings
(
    id       BIGINT NOT NULL,
    username VARCHAR(255),
    theme    VARCHAR(255),
    CONSTRAINT pk_user_settings PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT       NOT NULL,
    username VARCHAR(255) NOT NULL,
    roles    VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);