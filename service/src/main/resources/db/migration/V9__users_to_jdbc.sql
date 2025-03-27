alter sequence users_seq increment by 1;

select setval('users_seq', (select max(id) from users) + 1);

ALTER TABLE users
    ALTER COLUMN id SET DEFAULT nextval('users_seq');

alter sequence users_seq owned by users.id;