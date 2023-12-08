CREATE TABLE places
(
    id       SERIAL PRIMARY KEY,
    row      INT NOT NULL,
    seat     INT NOT NULL,
    bought BOOLEAN
);

INSERT INTO places(row, seat, bought)
VALUES (1, 1, TRUE),
       (1, 2, TRUE),
       (1, 3, TRUE),
       (2, 1, TRUE),
       (2, 2, TRUE),
       (2, 3, TRUE),
       (3, 1, TRUE),
       (3, 2, TRUE),
       (3, 3, TRUE)
