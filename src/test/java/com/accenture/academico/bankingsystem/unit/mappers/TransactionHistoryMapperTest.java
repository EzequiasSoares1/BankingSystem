package com.accenture.academico.bankingsystem.unit.mappers;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.domain.transation_history.TransactionHistory;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryResponseDTO;
import com.accenture.academico.bankingsystem.mappers.transaction_history.TransactionHistoryMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TransactionHistoryMapperTest {
    @Test
    void testeConvertToTransactionHistoryResponseDTOList(){
        User user = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        Address address = new Address(UUID.randomUUID(), "58700010", "888", "Rua Teste", "Centro", LocalDateTime.now(), LocalDateTime.now());
        Agency agency = new Agency(UUID.randomUUID(), "Teste", "83988776655", "888", address, LocalDateTime.now(), LocalDateTime.now());
        Client client = new Client(UUID.randomUUID(), "Teste", "11111", "8399888", address, user, LocalDateTime.now(), LocalDateTime.now());
        Account account = new Account(UUID.randomUUID(), "1111", AccountType.CURRENT, agency, BigDecimal.ZERO, client, LocalDateTime.now(), LocalDateTime.now());

        TransactionHistory transactionHistory1 = new TransactionHistory(UUID.randomUUID(), account, TransactionType.DEPOSIT, BigDecimal.TEN, LocalDateTime.now(), account.getBalance());
        TransactionHistory transactionHistory2 = new TransactionHistory(UUID.randomUUID(), account, TransactionType.WITHDRAW, BigDecimal.ONE, LocalDateTime.now(), account.getBalance());

        List<TransactionHistory> transactionHistoryList = List.of(transactionHistory1, transactionHistory2);
        List<TransactionHistoryResponseDTO> transactionHistoryResponseDTOList = TransactionHistoryMapper.convertToTransactionHistoryResponseDTOList(transactionHistoryList);

        assertNotEquals(transactionHistoryResponseDTOList.size(), 0);
        assertEquals(transactionHistoryResponseDTOList.get(0).transactionDate(), transactionHistoryList.get(0).getTransactionDate());
        assertEquals(transactionHistoryResponseDTOList.get(1).transactionDate(), transactionHistoryList.get(1).getTransactionDate());
        assertEquals(transactionHistoryResponseDTOList.get(0).transactionType(), transactionHistoryList.get(0).getTransactionType());
        assertEquals(transactionHistoryResponseDTOList.get(1).transactionType(), transactionHistoryList.get(1).getTransactionType());
        assertEquals(transactionHistoryResponseDTOList.get(0).amount(), transactionHistoryList.get(0).getAmount());
        assertEquals(transactionHistoryResponseDTOList.get(1).amount(), transactionHistoryList.get(1).getAmount());
        assertEquals(transactionHistoryResponseDTOList.get(0).balanceCurrent(), transactionHistoryList.get(0).getBalanceCurrent());
        assertEquals(transactionHistoryResponseDTOList.get(1).balanceCurrent(), transactionHistoryList.get(1).getBalanceCurrent());
    }
}
