/*
    User table
*/
create table users
(
    id       uuid not null
        primary key,
    username text not null,
    password text not null,
    owner_id uuid
        references owners on delete cascade
);
