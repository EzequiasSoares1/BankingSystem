package com.accenture.academico.bankingsystem.unit.mappers;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.mappers.transaction.TransactionMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionMapperTest {
    @Test
    void testConvertToTransactionResponseDTO(){
        User user = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        Address address = new Address(UUID.randomUUID(), "58700010", "888", "Rua Teste", "Centro", LocalDateTime.now(), LocalDateTime.now());
        Agency agency = new Agency(UUID.randomUUID(), "Teste", "83988776655", "888", address, LocalDateTime.now(), LocalDateTime.now());
        Client client = new Client(UUID.randomUUID(), "Teste", "11111", "8399888", address, user, LocalDateTime.now(), LocalDateTime.now());
        Account account = new Account(UUID.randomUUID(), "1111", AccountType.CURRENT, agency, BigDecimal.ZERO, client, LocalDateTime.now(), LocalDateTime.now());

        TransactionType transactionType = TransactionType.DEPOSIT;
        BigDecimal value = BigDecimal.valueOf(100);

        TransactionResponseDTO transactionResponseDTO = TransactionMapper.convertToTransactionResponseDTO(account, transactionType, value);

        assertNotNull(transactionResponseDTO);
        assertEquals(transactionResponseDTO.accountType(), account.getAccountType());
        assertEquals(transactionResponseDTO.transactionType(), transactionType);
        assertEquals(transactionResponseDTO.agencyId(), account.getAgency().getId());
        assertEquals(transactionResponseDTO.accountId(), account.getId());
        assertEquals(transactionResponseDTO.balance(), account.getBalance());
        assertEquals(transactionResponseDTO.dataTransaction(), account.getUpdatedDate());
        assertEquals(transactionResponseDTO.valueTransaction(), value);
    }
}
