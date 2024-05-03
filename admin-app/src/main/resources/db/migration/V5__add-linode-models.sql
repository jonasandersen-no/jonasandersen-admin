create table linode_instance
(
    id           bigint primary key auto_increment,
    status       varchar(255),
    label        varchar(255),
    ips          varchar(255),
    tags         varchar(255),
    volume_names varchar(255)
);

create table linode_volume
(
    id        bigint primary key auto_increment,
    label     varchar(255),
    status    varchar(255),
    linode_id bigint,
    constraint fk_linode_id foreign key (linode_id) references linode_instance (id)
);