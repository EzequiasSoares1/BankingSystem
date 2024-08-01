CREATE TABLE account (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    number VARCHAR(10) NOT NULL,
    account_type VARCHAR(255) NOT NULL,
    agency_id VARCHAR(255) NOT NULL,
    CONSTRAINT account_agency_id_fkey FOREIGN KEY (agency_id) REFERENCES agency (id) ON DELETE RESTRICT ON UPDATE CASCADE
);