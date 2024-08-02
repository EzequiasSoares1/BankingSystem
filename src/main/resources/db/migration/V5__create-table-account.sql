CREATE TABLE account (
    id BINARY(16) NOT NULL PRIMARY KEY,
    number VARCHAR(10) NOT NULL,
    account_type VARCHAR(255) NOT NULL,
    balance DECIMAL(19, 4),
    agency_id BINARY(16) NOT NULL,
    client_id BINARY(16) NOT NULL,
    CONSTRAINT account_agency_id_fkey FOREIGN KEY (agency_id) REFERENCES agency (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT account_client_id_fkey FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE RESTRICT ON UPDATE CASCADE
);