create table shortcut
(
    id          bigint auto_increment
        primary key,
    project     varchar(255) not null,
    shortcut    varchar(255) not null,
    description varchar(255) not null,
    constraint unique_project_shortcut
        unique (project, shortcut)
);