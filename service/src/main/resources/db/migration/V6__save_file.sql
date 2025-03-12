create table save_file
(
    id        serial
        constraint save_file_pk
            primary key,
    name      text not null,
    owner     bigint
        constraint save_file_users_id_fk
            references users,
    volume_id bigint,
    linode_id bigint
);
