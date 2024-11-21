CREATE TABLE books
(
    id      SERIAL PRIMARY KEY,
    title   varchar(1000) NOT NULL,
    price   NUMERIC(10, 2),
    status  CHAR(1),
    version BIGINT DEFAULT 0
);
