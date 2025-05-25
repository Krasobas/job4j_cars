create table if not exists post
(
    id            serial primary key,
    title varchar not null,
    description   varchar                       not null,
    price bigint not null,
    available boolean not null default true,
    has_photo boolean not null default false,
    created       timestamp without time zone not null default now(),
    updated       timestamp without time zone not null default now(),
    user_id  int not null references auto_user (id),
    car_id   int not null unique references car(id)
);