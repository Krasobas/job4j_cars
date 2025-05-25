create table if not exists car (
    id serial primary key,
    brand_id int references brand(id),
    model varchar not null,
    body_type_id int references body_type(id),
    engine_id int references engine(id),
    color_id int references color(id),
    manufacture_year int,
    mileage int
);