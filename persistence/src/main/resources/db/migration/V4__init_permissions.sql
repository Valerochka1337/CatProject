/*
 Permission table
 */
create table permissions
(
    id   serial not null
        primary key,
    name text   not null
);

/*
 Many-to-many roles-permissions table
 */
create table roles_permissions
(
    role_id       integer not null
        references roles on delete cascade,
    permission_id integer not null
        references permissions on delete cascade
);