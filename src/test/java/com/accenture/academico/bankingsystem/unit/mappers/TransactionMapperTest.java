package com.accenture.academico.bankingsystem.unit.mappers;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransferResponseDTO;
import com.accenture.academico.bankingsystem.dtos.pix_key.PixRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;
import com.accenture.academico.bankingsystem.mappers.transaction.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionMapperTest {

    private User user;
    private Address address;
    private Agency agency;
    private Client client;
    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setup() {
        user = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        address = new Address(UUID.randomUUID(), "58700010", "888", "Rua Teste", "Centro", LocalDateTime.now(), LocalDateTime.now());
        agency = new Agency(UUID.randomUUID(), "Teste", "83988776655", "888", address, LocalDateTime.now(), LocalDateTime.now());
        client = new Client(UUID.randomUUID(), "Teste", "11111", "8399888", address, user, LocalDateTime.now(), LocalDateTime.now());
        fromAccount = new Account(UUID.randomUUID(), "1111", AccountType.CURRENT, agency, BigDecimal.valueOf(500), client, LocalDateTime.now(), LocalDateTime.now());
        toAccount = new Account(UUID.randomUUID(), "2222", AccountType.SAVINGS, agency, BigDecimal.valueOf(300), client, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void testConvertToTransactionResponseDTO() {
        TransactionType transactionType = TransactionType.DEPOSIT;
        BigDecimal valueTransaction = BigDecimal.valueOf(100);

        TransactionResponseDTO transactionResponseDTO = TransactionMapper.convertToTransactionResponseDTO(fromAccount, transactionType, valueTransaction);

        assertNotNull(transactionResponseDTO);
        assertEquals(transactionResponseDTO.accountType(), fromAccount.getAccountType());
        assertEquals(transactionResponseDTO.transactionType(), transactionType);
        assertEquals(transactionResponseDTO.agencyId(), fromAccount.getAgency().getId());
        assertEquals(transactionResponseDTO.accountId(), fromAccount.getId());
        assertEquals(transactionResponseDTO.balance(), fromAccount.getBalance());
        assertEquals(transactionResponseDTO.dataTransaction(), fromAccount.getUpdatedDate());
        assertEquals(transactionResponseDTO.valueTransaction(), valueTransaction);
    }

    @Test
    void testConvertToTransactionHistoryRequestDTO() {
        TransactionType transactionType = TransactionType.DEPOSIT;
        BigDecimal value = BigDecimal.valueOf(100);
        TransactionResponseDTO transactionResponseDTO = TransactionMapper.convertToTransactionResponseDTO(fromAccount, transactionType, value);

        TransactionHistoryRequestDTO transactionHistoryRequestDTO = TransactionMapper.convertToTransactionHistoryRequestDTO(transactionResponseDTO);

        assertNotNull(transactionHistoryRequestDTO);
        assertEquals(transactionHistoryRequestDTO.accountId(), transactionResponseDTO.accountId());
        assertEquals(transactionHistoryRequestDTO.transactionType(), transactionResponseDTO.transactionType());
        assertEquals(transactionHistoryRequestDTO.value(), transactionResponseDTO.valueTransaction());
        assertEquals(transactionHistoryRequestDTO.balanceCurrent(), transactionResponseDTO.balance());
    }

    @Test
    void testConvertToTransferResponseDTO() {
        TransactionRequestDTO transactionDTO = new TransactionRequestDTO(AccountType.CURRENT, toAccount.getId(), BigDecimal.valueOf(50));
        TransactionType transactionType = TransactionType.TRANSFER;

        TransferResponseDTO transferResponseDTO = TransactionMapper.convertToTransferResponseDTO(fromAccount, toAccount, transactionDTO, transactionType);

        assertNotNull(transferResponseDTO);
        assertEquals(transferResponseDTO.senderId(), fromAccount.getId());
        assertEquals(transferResponseDTO.receiverId(), toAccount.getId());
        assertEquals(transferResponseDTO.senderBalance(), fromAccount.getBalance());
        assertEquals(transferResponseDTO.receiverBalance(), toAccount.getBalance());
        assertEquals(transferResponseDTO.accountType(), fromAccount.getAccountType());
        assertEquals(transferResponseDTO.transactionType(), transactionType);
        assertEquals(transferResponseDTO.agencyId(), fromAccount.getAgency().getId());
        assertEquals(transferResponseDTO.dataTransaction(), fromAccount.getUpdatedDate());
        assertEquals(transferResponseDTO.valueTransaction(), transactionDTO.value());
    }

    @Test
    void testConvertToPIXResponseDTO() {
        PixRequestDTO pixRequestDTO = new PixRequestDTO("ezequias@gmail.com", AccountType.CURRENT, BigDecimal.valueOf(75));
        TransactionType transactionType = TransactionType.PIX;

        TransferResponseDTO pixResponseDTO = TransactionMapper.convertToPIXResponseDTO(fromAccount, toAccount, pixRequestDTO, transactionType);

        assertNotNull(pixResponseDTO);
        assertEquals(pixResponseDTO.senderId(), fromAccount.getId());
        assertEquals(pixResponseDTO.receiverId(), toAccount.getId());
        assertEquals(pixResponseDTO.senderBalance(), fromAccount.getBalance());
        assertEquals(pixResponseDTO.receiverBalance(), toAccount.getBalance());
        assertEquals(pixResponseDTO.accountType(), fromAccount.getAccountType());
        assertEquals(pixResponseDTO.transactionType(), transactionType);
        assertEquals(pixResponseDTO.agencyId(), fromAccount.getAgency().getId());
        assertEquals(pixResponseDTO.dataTransaction(), fromAccount.getUpdatedDate());
        assertEquals(pixResponseDTO.valueTransaction(), pixRequestDTO.value());
    }
}

