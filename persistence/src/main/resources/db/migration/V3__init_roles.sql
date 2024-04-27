/*
    Role table
*/
create table roles
(
    id   serial not null
        primary key,
    name text   not null
);

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