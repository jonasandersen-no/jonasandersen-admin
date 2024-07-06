create table if not exists permitted_users
(
    id      bigint primary key auto_increment,
    subject varchar(255) not null unique,
    email   varchar(255) not null unique
);