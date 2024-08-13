package com.accenture.academico.bankingsystem.unit.domain;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.domain.transation_history.TransactionHistory;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionHistoryTest {

    @Test
    void testTransactionHistoryBuilder() {
        UUID id = UUID.randomUUID();
        Account account = new Account(); // Substitua por uma instância válida de Account
        TransactionType transactionType = TransactionType.DEPOSIT;
        BigDecimal amount = new BigDecimal("1000.00");
        LocalDateTime transactionDate = LocalDateTime.now();
        BigDecimal balanceCurrent = new BigDecimal("5000.00");

        TransactionHistory transactionHistory = new TransactionHistory(id, account, transactionType, amount, transactionDate, balanceCurrent);

        assertEquals(id, transactionHistory.getId());
        assertEquals(account, transactionHistory.getAccount());
        assertEquals(transactionType, transactionHistory.getTransactionType());
        assertEquals(amount, transactionHistory.getAmount());
        assertEquals(transactionDate, transactionHistory.getTransactionDate());
        assertEquals(balanceCurrent, transactionHistory.getBalanceCurrent());
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
        Account account = new Account(); // Substitua por uma instância válida de Account
        TransactionType transactionType = TransactionType.WITHDRAW;
        BigDecimal amount = new BigDecimal("200.00");
        LocalDateTime transactionDate = LocalDateTime.now();
        BigDecimal balanceCurrent = new BigDecimal("4800.00");

        transactionHistory.setId(id);
        transactionHistory.setAccount(account);
        transactionHistory.setTransactionType(transactionType);
        transactionHistory.setAmount(amount);
        transactionHistory.setTransactionDate(transactionDate);
        transactionHistory.setBalanceCurrent(balanceCurrent);

        assertEquals(id, transactionHistory.getId());
        assertEquals(account, transactionHistory.getAccount());
        assertEquals(transactionType, transactionHistory.getTransactionType());
        assertEquals(amount, transactionHistory.getAmount());
        assertEquals(transactionDate, transactionHistory.getTransactionDate());
        assertEquals(balanceCurrent, transactionHistory.getBalanceCurrent());
    }

    @Test
    void testPrePersist() {
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.prePersist();

        assertNotNull(transactionHistory.getTransactionDate());
        assertTrue(transactionHistory.getTransactionDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
