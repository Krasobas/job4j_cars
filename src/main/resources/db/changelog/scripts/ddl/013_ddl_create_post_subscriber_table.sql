create table if not exists post_subscriber (
   post_id int not null references post(id) on delete cascade,
   auto_user_id int not null references auto_user(id) on delete cascade,
   unique (post_id, auto_user_id)
);