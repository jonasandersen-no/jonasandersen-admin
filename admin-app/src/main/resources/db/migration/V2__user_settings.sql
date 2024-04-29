CREATE TABLE user_settings
(
    id    BIGINT       NOT NULL,
    theme VARCHAR(255) NULL,
    CONSTRAINT pk_user_settings PRIMARY KEY (id)
);