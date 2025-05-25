create table if not exists photo (
    id serial primary key,
    path varchar not null,
    is_main boolean not null default false,
    post_id int not null references post(id)
);