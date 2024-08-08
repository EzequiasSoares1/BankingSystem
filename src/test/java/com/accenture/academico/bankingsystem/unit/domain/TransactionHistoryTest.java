package com.accenture.academico.bankingsystem.unit.domain;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.domain.transation_history.TransactionHistory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionHistoryTest {

    @Test
    void testTransactionHistoryBuilder() {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        TransactionType transactionType = TransactionType.DEPOSIT;
        BigDecimal amount = new BigDecimal("100.00");
        LocalDateTime transactionDate = LocalDateTime.now();

        TransactionHistory transactionHistory = new TransactionHistory(id, account, transactionType, amount, transactionDate);

        assertEquals(id, transactionHistory.getId());
        assertEquals(account, transactionHistory.getAccount());
        assertEquals(transactionType, transactionHistory.getTransactionType());
        assertEquals(amount, transactionHistory.getAmount());
        assertEquals(transactionDate, transactionHistory.getTransactionDate());
    }

    @Test
    void testDefaultConstructor() {
        TransactionHistory transactionHistory = new TransactionHistory();

        assertNotNull(transactionHistory);
    }

    @Test
    void testSettersAndGetters() {
        TransactionHistory transactionHistory = new TransactionHistory();
        UUID id = UUID.randomUUID();
        Account account = new Account();
        TransactionType transactionType = TransactionType.WITHDRAWAL;
        BigDecimal amount = new BigDecimal("50.00");
        LocalDateTime transactionDate = LocalDateTime.now();

        transactionHistory.setId(id);
        transactionHistory.setAccount(account);
        transactionHistory.setTransactionType(transactionType);
        transactionHistory.setAmount(amount);
        transactionHistory.setTransactionDate(transactionDate);

        assertEquals(id, transactionHistory.getId());
        assertEquals(account, transactionHistory.getAccount());
        assertEquals(transactionType, transactionHistory.getTransactionType());
        assertEquals(amount, transactionHistory.getAmount());
        assertEquals(transactionDate, transactionHistory.getTransactionDate());
    }
}
