ALTER TABLE users ADD COLUMN activation_digest VARCHAR;
ALTER TABLE users ADD COLUMN activated BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE users ADD COLUMN activated_at TIMESTAMP;