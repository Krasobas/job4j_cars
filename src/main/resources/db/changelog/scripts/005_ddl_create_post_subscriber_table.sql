CREATE TABLE IF NOT EXISTS post_subscriber (
   auto_post_id INT NOT NULL REFERENCES auto_post(id),
   auto_user_id INT NOT NULL REFERENCES auto_user(id),
   UNIQUE (auto_post_id, auto_user_id)
);