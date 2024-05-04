use admin;
create table if not exists shortcut
(
    id          bigint primary key auto_increment,
    project     varchar(255) not null,
    shortcut    varchar(255) not null,
    description varchar(255) not null,
    CONSTRAINT unique_project_shortcut UNIQUE (project, shortcut)

);
