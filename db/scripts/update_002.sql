CREATE TABLE seats
(
    id     SERIAL PRIMARY KEY,
    row    INT NOT NULL,
    cell   INT NOT NULL,
    status BOOLEAN
);


INSERT INTO seats(row, cell)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (2, 3)
