package com.accenture.academico.bankingsystem.unit.mappers;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.account.AccountResponseDTO;
import com.accenture.academico.bankingsystem.mappers.account.AccountMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountMapperTest {
    @Test
    void testConvertToAccountResponseDTO(){
        User user = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        Address address = new Address(UUID.randomUUID(), "58700010", "888", "Rua Teste", "Centro", LocalDateTime.now(), LocalDateTime.now());
        Agency agency = new Agency(UUID.randomUUID(), "Teste", "83988776655", "888", address, LocalDateTime.now(), LocalDateTime.now());
        Client client = new Client(UUID.randomUUID(), "Teste", "11111", "8399888", address, user, LocalDateTime.now(), LocalDateTime.now());

        Account account = new Account(UUID.randomUUID(), "1111", AccountType.CURRENT, agency, BigDecimal.ZERO, client, LocalDateTime.now(), LocalDateTime.now());

        AccountResponseDTO accountResponseDTO = AccountMapper.convertToAccountResponseDTO(account);

        assertNotNull(accountResponseDTO);
        assertEquals(accountResponseDTO.id(), account.getId());
        assertEquals(accountResponseDTO.number(), account.getNumber());
        assertEquals(accountResponseDTO.accountType(), account.getAccountType());
        assertEquals(accountResponseDTO.agencyId(), account.getAgency().getId());
        assertEquals(accountResponseDTO.balance(), account.getBalance());
        assertEquals(accountResponseDTO.clientId(), account.getClient().getId());
    }

    @Test
    void testConvertToAccountResponseDTOList(){
        User user = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        Address address = new Address(UUID.randomUUID(), "58700010", "888", "Rua Teste", "Centro", LocalDateTime.now(), LocalDateTime.now());
        Agency agency = new Agency(UUID.randomUUID(), "Teste", "83988776655", "888", address, LocalDateTime.now(), LocalDateTime.now());
        Client client = new Client(UUID.randomUUID(), "Teste", "11111", "8399888", address, user, LocalDateTime.now(), LocalDateTime.now());

        Account account1 = new Account(UUID.randomUUID(), "1111", AccountType.CURRENT, agency, BigDecimal.ZERO, client, LocalDateTime.now(), LocalDateTime.now());
        Account account2 = new Account(UUID.randomUUID(), "2222", AccountType.SAVINGS, agency, BigDecimal.ZERO, client, LocalDateTime.now(), LocalDateTime.now());

        List<Account> accountList = List.of(account1, account2);

        List<AccountResponseDTO> accountResponseDTOList = AccountMapper.convertToAccountResponseDTOList(accountList);

        assertNotEquals(accountResponseDTOList.size(), 0);
        assertEquals(accountResponseDTOList.get(0).id(), accountList.get(0).getId());
        assertEquals(accountResponseDTOList.get(1).id(), accountList.get(1).getId());
        assertEquals(accountResponseDTOList.get(0).number(), accountList.get(0).getNumber());
        assertEquals(accountResponseDTOList.get(1).number(), accountList.get(1).getNumber());
        assertEquals(accountResponseDTOList.get(0).accountType(), accountList.get(0).getAccountType());
        assertEquals(accountResponseDTOList.get(1).accountType(), accountList.get(1).getAccountType());
        assertEquals(accountResponseDTOList.get(0).agencyId(), accountList.get(0).getAgency().getId());
        assertEquals(accountResponseDTOList.get(1).agencyId(), accountList.get(1).getAgency().getId());
        assertEquals(accountResponseDTOList.get(0).balance(), accountList.get(0).getBalance());
        assertEquals(accountResponseDTOList.get(1).balance(), accountList.get(1).getBalance());
        assertEquals(accountResponseDTOList.get(0).clientId(), accountList.get(0).getClient().getId());
        assertEquals(accountResponseDTOList.get(1).clientId(), accountList.get(1).getClient().getId());
    }
}
