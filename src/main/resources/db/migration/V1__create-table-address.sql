CREATE TABLE address
(
    id CHAR(36) NOT NULL PRIMARY KEY,
    cep      VARCHAR(10)  NOT NULL,
    number   VARCHAR(10),
    street   VARCHAR(100) NOT NULL,
    district VARCHAR(100) NOT NULL
);

CREATE TABLE users
(
     id CHAR(36) NOT NULL PRIMARY KEY,
     email VARCHAR(255) NOT NULL,
     password VARCHAR(255) NOT NULL,
     role VARCHAR(255) NOT NULL,
     client_id VARCHAR(255) NOT NULL
);