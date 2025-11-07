CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    remind_at TIMESTAMP NULL,
    email VARCHAR(255) NULL,
    reminded_at TIMESTAMP NULL
);

CREATE INDEX IF NOT EXISTS idx_tasks_remind_at ON tasks(remind_at);
CREATE INDEX IF NOT EXISTS idx_tasks_reminded_at ON tasks(reminded_at);
