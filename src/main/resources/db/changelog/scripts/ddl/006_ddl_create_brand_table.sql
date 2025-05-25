create table if not exists brand (
    id serial primary key,
    name varchar unique not null,
    country_id int not null references country(id)
);