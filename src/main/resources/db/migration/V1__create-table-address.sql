CREATE TABLE address
(
    id BINARY(16) NOT NULL PRIMARY KEY,
    cep      VARCHAR(10)  NOT NULL,
    number   VARCHAR(10),
    street   VARCHAR(100) NOT NULL,
    district VARCHAR(100) NOT NULL,
    created_date DATETIME,
    updated_date DATETIME
);



