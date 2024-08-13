package com.accenture.academico.bankingsystem.integrate.services;
import com.accenture.academico.bankingsystem.config.ConfigIntegrateSpringTest;
import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.domain.transation_history.TransactionHistory;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.repositories.*;
import com.accenture.academico.bankingsystem.services.TransactionHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionHistoryServiceTestIntegrate implements ConfigIntegrateSpringTest {

    @Autowired
    private TransactionHistoryService transactionHistoryService;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AgencyRepository agencyRepository;

    private Account account;
    private Account account2;
    private TransactionHistory transactionHistory;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("testUser@email.com");
        user.setPassword("password");
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);

        SecurityContextHolder.setContext(new SecurityContextImpl(
                new UsernamePasswordAuthenticationToken(user, "password", user.getAuthorities())
        ));

        Address address = new Address(UUID.randomUUID(), "58700-010", "872", "Rua do Prado", "Centro");
        address = addressRepository.save(address);

        Client client = new Client();
        client.setName("Cliente Teste");
        client.setCpf("12345678901");
        client.setTelephone("123456789");
        client.setAddress(address);
        client.setUser(user);

        client = clientRepository.save(client);

        Agency createdAgency = new Agency(UUID.randomUUID(), "Agência Liberdade", "987654321","436354",address);
        createdAgency = agencyRepository.save(createdAgency);

        account = new Account(UUID.randomUUID(), "232", AccountType.SAVINGS,createdAgency, BigDecimal.TEN, client); // Initialize account with necessary fields
        account = accountRepository.save(account);

        account2 = new Account(UUID.randomUUID(), "23232", AccountType.CURRENT,createdAgency, BigDecimal.TEN, client); // Initialize account with necessary fields
        account2 = accountRepository.save(account2);

        transactionHistory = new TransactionHistory();
        transactionHistory.setId(UUID.randomUUID());
        transactionHistory.setAccount(account);
        transactionHistory.setTransactionType(TransactionType.DEPOSIT);
        transactionHistory.setAmount(BigDecimal.valueOf(100));
        transactionHistory.setBalanceCurrent(BigDecimal.valueOf(100));
        transactionHistory.setTransactionDate(LocalDateTime.now());

        transactionHistoryRepository.save(transactionHistory);
    }

    @Test
    void createTransactionHistory() {
        TransactionHistoryRequestDTO request = new TransactionHistoryRequestDTO(
                account2.getId(),
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(50)
        );

        transactionHistoryService.createTransactionHistory(request);

        var savedHistory = transactionHistoryRepository.findByAccountId(account.getId());
        assertFalse(savedHistory.isEmpty());
        assertEquals(1, savedHistory.size());
    }

    @Test
    void createTransactionHistory_NotFound() {
        TransactionHistoryRequestDTO request = new TransactionHistoryRequestDTO(
                UUID.randomUUID(),
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(50)
        );

        assertThrows(NotFoundException.class, () -> {
            transactionHistoryService.createTransactionHistory(request);
        });
    }

    @Test
    void getAllTransactionHistoryByAccountId() {
        var historyList = transactionHistoryService.getAllTransactionHistoryByAccountId(account.getId());
        assertNotNull(historyList);
        assertNotEquals(0,historyList.size());
        assertEquals(transactionHistory.getTransactionType(), historyList.get(0).transactionType());
    }

    @Test
    void getAllTransactionHistoryByAccountId_NoHistory() {
        var historyList = transactionHistoryService.getAllTransactionHistoryByAccountId(account2.getId());
        assertNotNull(historyList);
        assertTrue(historyList.isEmpty());
    }

    @Test
    void getAllTransactionHistoryByAccountId_NotFound() {
        var historyList =  transactionHistoryService.getAllTransactionHistoryByAccountId(UUID.randomUUID());
        assertEquals(0,historyList.size());
    }
}
