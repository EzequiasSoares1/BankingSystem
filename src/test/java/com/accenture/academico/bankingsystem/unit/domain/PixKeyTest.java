package com.accenture.academico.bankingsystem.unit.domain;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.enums.PixKeyType;
import com.accenture.academico.bankingsystem.domain.pix_key.PixKey;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PixKeyTest {

    @Test
    void testPixKeyBuilder() {
        UUID id = UUID.randomUUID();
        PixKeyType keyType = PixKeyType.CPF;
        String keyValue = "12345678900";
        Account account = new Account(); // Substitua por uma instância válida de Account
        LocalDateTime createdDate = LocalDateTime.now();

        PixKey pixKey = new PixKey(id, keyType, keyValue, account, createdDate);

        assertEquals(id, pixKey.getId());
        assertEquals(keyType, pixKey.getKeyType());
        assertEquals(keyValue, pixKey.getKeyValue());
        assertEquals(account, pixKey.getAccount());
        assertEquals(createdDate, pixKey.getCreatedDate());
    }

    @Test
    void testDefaultConstructor() {
        PixKey pixKey = new PixKey();

        assertNotNull(pixKey);
    }

    @Test
    void testSettersAndGetters() {
        PixKey pixKey = new PixKey();
        UUID id = UUID.randomUUID();
        PixKeyType keyType = PixKeyType.EMAIL;
        String keyValue = "example@example.com";
        Account account = new Account(); // Substitua por uma instância válida de Account
        LocalDateTime createdDate = LocalDateTime.now();

        pixKey.setId(id);
        pixKey.setKeyType(keyType);
        pixKey.setKeyValue(keyValue);
        pixKey.setAccount(account);
        pixKey.setCreatedDate(createdDate);

        assertEquals(id, pixKey.getId());
        assertEquals(keyType, pixKey.getKeyType());
        assertEquals(keyValue, pixKey.getKeyValue());
        assertEquals(account, pixKey.getAccount());
        assertEquals(createdDate, pixKey.getCreatedDate());
    }

    @Test
    void testPrePersist() {
        PixKey pixKey = new PixKey();
        pixKey.prePersist();

        assertNotNull(pixKey.getCreatedDate());
        assertTrue(pixKey.getCreatedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
