/*
    Owner table
*/
create table owner
(
    id         uuid not null
        constraint owners_pkey
            primary key,
    name       text not null,
    birth_date date not null
);

alter table owner
    owner to postgres;


/*
    Cat table
*/
create table cat
(
    id         uuid not null
        primary key,
    name       text not null,
    color      text,
    breed      text,
    owner_id   uuid
                    references owner on delete set null,
    birth_date date not null
);

alter table cat
    owner to postgres;

/*
    Cat friendship many-to-many table
*/
create table cat_friendship
(
    cat1_id uuid not null
        references cat on delete cascade,
    cat2_id uuid not null
        references cat on delete cascade
);

alter table cat_friendship
    owner to postgres;