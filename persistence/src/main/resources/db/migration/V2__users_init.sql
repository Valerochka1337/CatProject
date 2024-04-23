/*
    User table
*/
create table users
(
    id       uuid not null
        primary key,
    username text not null,
    password text not null,
    owner_id uuid not null
        references owners on delete cascade
);


/*
    Role table
*/
create table roles
(
    id   SERIAL not null
        primary key,
    name text   not null
);

insert into roles(name)
values ('ADMIN'),
       ('USER');

/*
    User-Role many-to-many table
*/
create table users_roles
(
    user_id uuid    not null
        references users on delete cascade,
    role_id integer not null
        references roles on delete cascade
);