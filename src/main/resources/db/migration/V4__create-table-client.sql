CREATE TABLE client (
    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    address_id BINARY(16) NOT NULL,
    bank_user_id BINARY(16) NOT NULL,
    created_date DATETIME,
    updated_date DATETIME,
    CONSTRAINT client_address_id_fkey FOREIGN KEY (address_id) REFERENCES address (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT client_bank_user_id_fkey FOREIGN KEY (bank_user_id) REFERENCES bank_user (id) ON DELETE RESTRICT ON UPDATE CASCADE
);