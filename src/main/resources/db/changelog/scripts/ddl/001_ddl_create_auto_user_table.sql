create table if not exists auto_user
(
    id        serial primary key,
    name      varchar        not null,
    role      varchar not null default 'user',
    email     varchar unique not null,
    password  varchar        not null
);