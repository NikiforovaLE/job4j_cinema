CREATE TABLE accounts
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    phone    VARCHAR NOT NULL UNIQUE,
    email    VARCHAR UNIQUE
);

CREATE TABLE tickets
(
    id         SERIAL PRIMARY KEY,
    seat_id    INT NOT NULL REFERENCES places (id),
    account_id INT NOT NULL REFERENCES accounts (id),
    session_id INT NOT NULL
);