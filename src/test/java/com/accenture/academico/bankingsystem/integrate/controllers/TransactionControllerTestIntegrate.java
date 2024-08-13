package com.accenture.academico.bankingsystem.integrate.controllers;

import com.accenture.academico.bankingsystem.config.ConfigIntegrateSpringTest;
import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.PixKeyType;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.pix_key.PixKey;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.pix_key.PixRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.OperationRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryResponseDTO;
import com.accenture.academico.bankingsystem.repositories.*;
import com.accenture.academico.bankingsystem.services.TransactionHistoryService;
import com.accenture.academico.bankingsystem.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.util.UUID;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTestIntegrate implements ConfigIntegrateSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @Autowired
    private PixKeyRepository pixKeyRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Account account;
    private PixKey pixKey;
    private Account account2;
    private PixKey pixKey2;

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

        account = new Account(UUID.randomUUID(), "232", AccountType.SAVINGS, createdAgency, BigDecimal.TEN, client);
        account = accountRepository.save(account);

        account2 = new Account(UUID.randomUUID(), "232", AccountType.SAVINGS, createdAgency, BigDecimal.TEN, client);
        account2 = accountRepository.save(account);

        pixKey = new PixKey();
        pixKey.setId(UUID.randomUUID());
        pixKey.setKeyType(PixKeyType.TELEPHONE);
        pixKey.setKeyValue("834826637");
        pixKey.setAccount(account);

        pixKey = pixKeyRepository.save(pixKey);

        pixKey2 = new PixKey();
        pixKey2.setId(UUID.randomUUID());
        pixKey2.setKeyType(PixKeyType.TELEPHONE);
        pixKey2.setKeyValue("8334632454");
        pixKey2.setAccount(account2);

        pixKey2 = pixKeyRepository.save(pixKey2);
    }

    @Test
    void testDeposit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"accountType\": \"SAVINGS\", \"value\": 100 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valueTransaction", is(100)))
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.transactionType", is("DEPOSIT")));
    }

    @Test
    void testDepositNegative() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"accountType\": \"SAVINGS\", \"value\": -100 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWithdraw() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"accountType\": \"SAVINGS\", \"value\": 100 }"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"accountType\": \"SAVINGS\", \"value\": 50 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valueTransaction", is(50)))
                .andExpect(jsonPath("$.transactionType", is("WITHDRAW")));
    }

    @Test
    void testWithdrawNotFunds() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"accountType\": \"SAVINGS\", \"value\": 5000 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testTransfer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"accountType\": \"SAVINGS\", \"value\": 400 }"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"accountType\": \"" + account.getAccountType() + "\", \"receiverId\": \"" + account2.getId() + "\", \"value\": 200 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valueTransaction", is(200)))
                .andExpect(jsonPath("$.transactionType", is("TRANSFER")));
    }
    @Test
    void testTransferNotFunds() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"accountType\": \"" + account.getAccountType() + "\", \"receiverId\": \"" + account2.getId() + "\", \"value\": 200 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testTransferNegative() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"accountType\": \"" + account.getAccountType() + "\", \"receiverId\": \"" + account2.getId() + "\", \"value\": -200 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPixNotFunds() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"pixKey\": \"8334632454\", \"accountType\": \"SAVINGS\", \"value\": 300 }"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testPix() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"accountType\": \"SAVINGS\", \"value\": 700 }"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"pixKey\": \"8334632454\", \"accountType\": \"SAVINGS\", \"value\": 300 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valueTransaction", is(300)))
                .andExpect(jsonPath("$.transactionType", is("PIX")));
    }

    @Test
    void testPixNegative() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"pixKey\": \"8334632454\", \"accountType\": \"SAVINGS\", \"value\": -300 }"))
                .andExpect(status().isBadRequest());
    }
}
