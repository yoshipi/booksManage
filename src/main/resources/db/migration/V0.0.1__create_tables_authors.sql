CREATE TABLE authors
(
    id         SERIAL PRIMARY KEY,
    name       varchar(1000) NOT NULL,
    birth_date DATE          NOT NULL,
    version    BIGINT DEFAULT 0
);