CREATE TABLE transaction_history (
                             id BINARY(16) NOT NULL PRIMARY KEY,
                             account_id BINARY(16) NOT NULL,
                             transaction_type VARCHAR(50) NOT NULL,
                             amount DECIMAL(19, 4) NOT NULL,
                             transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             balance_current DECIMAL(19, 4) NOT NULL,
                             CONSTRAINT transaction_history_account_id_fkey FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE RESTRICT ON UPDATE CASCADE
);