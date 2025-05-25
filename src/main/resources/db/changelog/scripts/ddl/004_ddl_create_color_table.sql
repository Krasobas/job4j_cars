create table if not exists color (
    id serial primary key,
    name varchar not null,
    hex_code varchar unique not null
);