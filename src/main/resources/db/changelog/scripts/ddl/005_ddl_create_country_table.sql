create table if not exists country (
    id serial primary key,
    name varchar unique not null
);