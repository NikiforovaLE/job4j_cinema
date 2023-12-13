CREATE TABLE accounts
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    phone    VARCHAR NOT NULL UNIQUE,
    email    VARCHAR UNIQUE
);

CREATE TABLE seats
(
    id       SERIAL PRIMARY KEY,
    row      INT NOT NULL,
    cell     INT NOT NULL,
    bought BOOLEAN
);

INSERT INTO seats(row, cell, bought)
VALUES (1, 1, FALSE),
       (1, 2, FALSE),
       (1, 3, FALSE),
       (2, 1, FALSE),
       (2, 2, FALSE),
       (2, 3, FALSE),
       (3, 1, FALSE),
       (3, 2, FALSE),
       (3, 3, FALSE);

CREATE TABLE tickets
(
    id         SERIAL PRIMARY KEY,
    seat_id    INT NOT NULL REFERENCES seats (id),
    account_id INT NOT NULL REFERENCES accounts (id),
    session_id TEXT NOT NULL
);