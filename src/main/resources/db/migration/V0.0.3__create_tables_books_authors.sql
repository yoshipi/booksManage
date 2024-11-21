CREATE TABLE books_authors
(
    books_id   INT,
    authors_id INT,
    PRIMARY KEY (books_id, authors_id),
    FOREIGN KEY (books_id) REFERENCES books (id),
    FOREIGN KEY (authors_id) REFERENCES authors (id)
);