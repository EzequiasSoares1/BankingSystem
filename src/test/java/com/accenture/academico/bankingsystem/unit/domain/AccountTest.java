package com.accenture.academico.bankingsystem.unit.domain;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    void testAccountBuilder() {
        UUID id = UUID.randomUUID();
        String number = "1234567890";
        AccountType accountType = AccountType.CURRENT;
        Agency agency = new Agency();
        BigDecimal balance = BigDecimal.valueOf(1000.00);
        Client client = new Client();

        Account account = new Account(id, number, accountType, agency, balance, client);

        assertEquals(id, account.getId());
        assertEquals(number, account.getNumber());
        assertEquals(accountType, account.getAccountType());
        assertEquals(agency, account.getAgency());
        assertEquals(balance, account.getBalance());
        assertEquals(client, account.getClient());
    }

    @Test
    void testDefaultConstructor() {
        Account account = new Account();

        assertNotNull(account);
    }

    @Test
    void testSettersAndGetters() {
        Account account = new Account();
        UUID id = UUID.randomUUID();
        String number = "0987654321";
        AccountType accountType = AccountType.SAVINGS;
        Agency agency = new Agency();
        BigDecimal balance = BigDecimal.valueOf(500.00);
        Client client = new Client();

        account.setId(id);
        account.setNumber(number);
        account.setAccountType(accountType);
        account.setAgency(agency);
        account.setBalance(balance);
        account.setClient(client);

        assertEquals(id, account.getId());
        assertEquals(number, account.getNumber());
        assertEquals(accountType, account.getAccountType());
        assertEquals(agency, account.getAgency());
        assertEquals(balance, account.getBalance());
        assertEquals(client, account.getClient());
    }

    @Test
    void testPrePersist() {
        Account account = new Account();
        account.prePersist();

        assertNotNull(account.getCreatedDate());
        assertTrue(account.getCreatedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdate() {
        Account account = new Account();
        account.prePersist();
        LocalDateTime initialUpdatedDate = account.getUpdatedDate();

        account.preUpdate();

        assertEquals(initialUpdatedDate, account.getUpdatedDate());
        assertTrue(account.getUpdatedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

}
