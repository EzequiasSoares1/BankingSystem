CREATE TABLE pix_key(
    id BINARY(16) NOT NULL PRIMARY KEY,
    key_type VARCHAR(255) NOT NULL,
    key_value VARCHAR(255) NOT NULL UNIQUE,
    account_id BINARY(16) NOT NULL,
    created_date DATETIME,
    CONSTRAINT pix_key_account_id_fkey FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE RESTRICT ON UPDATE CASCADE
)