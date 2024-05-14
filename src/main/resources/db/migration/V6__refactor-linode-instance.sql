alter table linode_instance
    drop column status,
    drop column label,
    drop column ips,
    drop column tags,
    drop column volume_names;

alter table linode_instance
    add column linode_id    bigint       null unique,
    add column created_by   varchar(255) null,
    add column created_date datetime     null,
    add column server_type  varchar(255) null,
    add column sub_domain   varchar(255) null;
