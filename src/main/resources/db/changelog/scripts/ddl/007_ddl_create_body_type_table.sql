create table if not exists body_type (
    id serial primary key,
    name varchar unique not null
);