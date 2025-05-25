create table if not exists engine (
    id serial primary key,
    name varchar not null,
    volume float(2),
    fuel_type varchar not null,
    power_hp int
);