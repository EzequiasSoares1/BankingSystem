CREATE TABLE bank_user

(
     id CHAR(36) NOT NULL PRIMARY KEY,
     email VARCHAR(255) NOT NULL,
     password VARCHAR(255) NOT NULL,
     role VARCHAR(255) NOT NULL,
     created_date DATETIME,
     updated_date DATETIME
);