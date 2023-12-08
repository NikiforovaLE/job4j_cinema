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
    row        INT NOT NULL,
    seat       INT NOT NULL,
    session_id INT NOT NULL,
    account_id INT NOT NULL REFERENCES accounts (id)
);