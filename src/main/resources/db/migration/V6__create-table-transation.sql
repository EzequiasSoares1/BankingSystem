CREATE TABLE transaction_history (
                     id CHAR(36) NOT NULL,
                     account_id BINARY(16) NOT NULL,
                     transaction_type VARCHAR(50) NOT NULL,
                     amount DECIMAL(19, 4) NOT NULL,
                     transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     created_date DATETIME,
                     updated_date DATETIME,
                     PRIMARY KEY (id),
                     FOREIGN KEY (account_id) REFERENCES account(id)
);