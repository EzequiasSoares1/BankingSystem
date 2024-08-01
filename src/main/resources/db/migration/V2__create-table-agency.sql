CREATE TABLE agency (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    number VARCHAR(10) NOT NULL,
    address_id VARCHAR(255) NOT NULL,
    CONSTRAINT agency_address_id_fkey FOREIGN KEY (address_id) REFERENCES address (id) ON DELETE RESTRICT ON UPDATE CASCADE
);