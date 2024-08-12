package com.accenture.academico.bankingsystem.integrate.services;

import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.account.AccountRequestDTO;
import com.accenture.academico.bankingsystem.dtos.account.AccountUpdateDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.OperationRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionRequestDTO;
import com.accenture.academico.bankingsystem.exceptions.AmountNegativeException;
import com.accenture.academico.bankingsystem.exceptions.ConflictException;
import com.accenture.academico.bankingsystem.exceptions.InsufficientFundsException;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.repositories.*;
import com.accenture.academico.bankingsystem.services.AccountService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountServiceTest implements ConfigSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private Client client;
    private Agency agency;
    private Account account;
    private Address address;


    @BeforeEach
    void setUp() {

        address = new Address(UUID.randomUUID(), "58700-010", "872", "Rua do Prado", "Centro");
        address = addressRepository.save(address);

        agency = new Agency(UUID.randomUUID(), "1234", "Main Branch","5433245",address);
        agency = agencyRepository.save(agency);

        User user = new User();
        user.setEmail("testUser@email.com");
        user.setPassword("password");
        user.setRole(Role.ADMIN);

        userRepository.save(user);
        client = new Client();
        client.setName("Test Client");
        client.setCpf("12345678901");
        client.setTelephone("123456789");
        client.setAddress(address);
        client.setUser(user);
        client = clientRepository.save(client);

        Authentication mockAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(mockAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        account = new Account();
        account.setNumber("123456");
        account.setAccountType(AccountType.CURRENT);
        account.setBalance(BigDecimal.valueOf(1500));
        account.setClient(client);
        account.setAgency(agency);
        account = accountRepository.save(account);
    }

    @Test
    @Order(1)
    void deposit_ValidAmount() {
        BigDecimal depositAmount = BigDecimal.valueOf(500);
        var updatedAccount = accountService.deposit(new OperationRequestDTO(AccountType.CURRENT, depositAmount));

        assertNotNull(updatedAccount);
        assertEquals(account.getBalance(), updatedAccount.balance());
    }

    @Test
    @Order(2)
    void deposit_NegativeAmount() {
        BigDecimal negativeAmount = BigDecimal.valueOf(-100);

        assertThrows(AmountNegativeException.class, () -> {
            accountService.deposit(new OperationRequestDTO(AccountType.CURRENT, negativeAmount));
        });
    }

    @Test
    @Order(3)
    void withdraw_ValidAmount() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(200);
        var updatedAccount = accountService.withdraw(new OperationRequestDTO(AccountType.CURRENT, withdrawAmount));

        assertNotNull(updatedAccount);
        assertEquals(account.getBalance(), updatedAccount.balance());
    }

    @Test
    @Order(4)
    void withdraw_InsufficientFunds() {
        BigDecimal excessiveAmount = BigDecimal.valueOf(5000);

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.withdraw(new OperationRequestDTO(AccountType.CURRENT, excessiveAmount));
        });
    }

    @Test
    @Order(5)
    void withdraw_NegativeAmount() {
        BigDecimal negativeAmount = BigDecimal.valueOf(-100);

        assertThrows(AmountNegativeException.class, () -> {
            accountService.withdraw(new OperationRequestDTO(AccountType.CURRENT, negativeAmount));
        });
    }

    @Test
    @Order(6)
    void transfer_ValidAmount() {
        Account toAccount = new Account();
        toAccount.setNumber("654321");
        toAccount.setAccountType(AccountType.SAVINGS);
        toAccount.setBalance(BigDecimal.valueOf(500));
        toAccount.setClient(client);
        toAccount.setAgency(agency);
        toAccount = accountRepository.save(toAccount);

        BigDecimal transferAmount = BigDecimal.valueOf(300);
        accountService.transfer(new TransactionRequestDTO(AccountType.CURRENT, toAccount.getId(), transferAmount));

        Account updatedFromAccount = accountRepository.findById(account.getId()).orElseThrow();
        Account updatedToAccount = accountRepository.findById(toAccount.getId()).orElseThrow();

        assertEquals(account.getBalance(), updatedFromAccount.getBalance());
        assertEquals(toAccount.getBalance(), updatedToAccount.getBalance());
    }

    @Test
    @Order(7)
    void transfer_InsufficientFunds() {
        Account toAccount = new Account();
        toAccount.setNumber("654321");
        toAccount.setAccountType(AccountType.SAVINGS);
        toAccount.setBalance(BigDecimal.valueOf(500));
        toAccount.setClient(client);
        toAccount.setAgency(agency);
        toAccount = accountRepository.save(toAccount);

        BigDecimal excessiveAmount = BigDecimal.valueOf(3000);

        Account finalToAccount = toAccount;
        assertThrows(InsufficientFundsException.class, () -> {
            accountService.transfer(new TransactionRequestDTO(AccountType.CURRENT, finalToAccount.getId(), excessiveAmount));
        });
    }

    @Test
    @Order(8)
    void transfer_NegativeAmount() {

        Account toAccount = new Account();
        toAccount.setNumber("654321");
        toAccount.setAccountType(AccountType.SAVINGS);
        toAccount.setBalance(BigDecimal.valueOf(500));
        toAccount.setClient(client);
        toAccount.setAgency(agency);
        toAccount = accountRepository.save(toAccount);

        BigDecimal negativeAmount = BigDecimal.valueOf(-100);

        Account finalToAccount = toAccount;
        assertThrows(AmountNegativeException.class, () -> {
            accountService.transfer(new TransactionRequestDTO(AccountType.CURRENT, finalToAccount.getId(), negativeAmount));
        });
    }
    @Test
    @Order(9)
    void createAccount_ValidRequest() {
        AccountRequestDTO requestDTO = new AccountRequestDTO(
                AccountType.SAVINGS, agency.getId()
        );

        var createdAccount = accountService.createAccount(requestDTO);

        assertNotNull(createdAccount);
        assertEquals(requestDTO.accountType(), createdAccount.accountType());
        assertEquals(BigDecimal.ZERO, createdAccount.balance());
    }

    @Test
    @Order(10)
    void createAccount_Conflict() {
        AccountRequestDTO requestDTO = new AccountRequestDTO(
                account.getAccountType(), agency.getId()
        );

        assertThrows(ConflictException.class, () -> {
            accountService.createAccount(requestDTO);
        });
    }

    @Test
    @Order(11)
    void createAccount_InvalidAgency() {
        UUID invalidAgencyId = UUID.randomUUID();
        AccountRequestDTO requestDTO = new AccountRequestDTO(
                 AccountType.SAVINGS, invalidAgencyId
        );

        assertThrows(NotFoundException.class, () -> {
            accountService.createAccount(requestDTO);
        });
    }

    @Test
    @Order(12)
    void updateAccount_ValidRequest() {
        AccountUpdateDTO updateDTO = new AccountUpdateDTO(AccountType.SAVINGS, agency.getId());

        var updatedAccount = accountService.updateAccount(account.getId(), updateDTO);

        assertNotNull(updatedAccount);
        assertEquals(AccountType.SAVINGS, updatedAccount.accountType());
        assertEquals(agency.getId(), updatedAccount.agencyId());
    }

    @Test
    @Order(13)
    void updateAccount_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        AccountUpdateDTO updateDTO = new AccountUpdateDTO(AccountType.SAVINGS, agency.getId());

        assertThrows(NotFoundException.class, () -> {
            accountService.updateAccount(nonExistentId, updateDTO);
        });
    }

    @Test
    @Order(14)
    void updateAccount_Conflict() {
        AccountUpdateDTO updateDTO = new AccountUpdateDTO(AccountType.CURRENT, agency.getId());

        assertThrows(ConflictException.class, () -> {
            accountService.updateAccount(account.getId(), updateDTO);
        });
    }

    @Test
    @Order(15)
    void deleteAccount() {
        UUID accountId = account.getId();

        accountService.deleteAccount(accountId);

        assertThrows(NotFoundException.class, () -> {
            accountService.getAccountById(accountId);
        });
    }

    @Test
    @Order(16)
    void deleteAccount_NotFound() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(NotFoundException.class, () -> {
            accountService.deleteAccount(nonExistentId);
        });
    }
}

