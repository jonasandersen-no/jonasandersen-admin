alter sequence permitted_users_seq increment by 1;

ALTER TABLE permitted_users
    ALTER COLUMN id SET DEFAULT nextval('permitted_users_seq');

alter sequence permitted_users_seq owned by permitted_users.id;