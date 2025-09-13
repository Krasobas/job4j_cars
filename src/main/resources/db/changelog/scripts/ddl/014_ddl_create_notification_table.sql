CREATE TABLE IF NOT EXISTS notification (
    id SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS notification_user (
    notification_id BIGINT REFERENCES notification(id),
    user_id BIGINT REFERENCES auto_user(id),
    PRIMARY KEY (notification_id, user_id)
);

CREATE INDEX idx_notification_user_unread ON notification_user(user_id);