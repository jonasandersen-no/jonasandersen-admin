
ALTER TABLE users
    ADD password VARCHAR(255);

ALTER TABLE users
    ADD CONSTRAINT uc_users_settings UNIQUE (settings);