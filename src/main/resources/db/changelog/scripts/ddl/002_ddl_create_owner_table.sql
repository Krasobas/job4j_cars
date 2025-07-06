create table if not exists owner (
    id serial primary key,
    name varchar unique not null
);