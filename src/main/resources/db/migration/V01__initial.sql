-- Initialize the database.
-- Drop any existing data and create empty tables.

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  username TEXT UNIQUE NOT NULL,
  password TEXT NOT NULL,
  phone_number TEXT UNIQUE NOT NULL,
  verified INTEGER NOT NULL DEFAULT 0
);

INSERT INTO sqlite_sequence (name, seq) VALUES ("userTableSeq", 1);
