alter table users
    add settings bigint;

alter table users
    add constraint users_user_settings_id_fk
        foreign key (settings) references user_settings;

alter table user_settings
    drop column username;