/*
    Owner table
*/
create table owners
(
    id         uuid not null
        primary key,
    name       text not null,
    birth_date date not null
);


/*
    Cat table
*/
create table cats
(
    id         uuid not null
        primary key,
    name       text not null,
    color      text,
    breed      text,
    owner_id   uuid
                    references owners on delete set null,
    birth_date date not null
);


/*
    Cat friendship many-to-many table
*/
create table cat_friendship
(
    cat1_id uuid not null
        references cats on delete cascade,
    cat2_id uuid not null
        references cats on delete cascade
);
