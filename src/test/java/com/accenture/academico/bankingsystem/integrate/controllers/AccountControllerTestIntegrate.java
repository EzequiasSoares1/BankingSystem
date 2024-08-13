package com.accenture.academico.bankingsystem.integrate.controllers;

import com.accenture.academico.bankingsystem.config.ConfigIntegrateSpringTest;
import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.repositories.*;
import com.accenture.academico.bankingsystem.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTestIntegrate implements ConfigIntegrateSpringTest {

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
    void getAllAccounts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAccountById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is("123456")));
    }

    @Test
    void createAccount() throws Exception {
        String newAccountJson = "{ \"accountType\": \"SAVINGS\", \"agencyId\": \"" + agency.getId() + "\", \"clientId\": \"" + client.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newAccountJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.balance", is(0)));
    }

    @Test
    void updateAccount() throws Exception {
        String updatedAccountJson = "{ \"accountType\": \"SAVINGS\", \"agencyId\": \"" + agency.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/account/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAccountJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.agencyId", is(agency.getId().toString())));
    }

    @Test
    void deleteAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/account/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void createAccount_WithInvalidData() throws Exception {
        String invalidData = "{ \"accountType\": \"\", \"agencyId\": \"" + agency.getId() + "\", \"clientId\": \"" + client.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidData))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateAccount_WithInvalidData() throws Exception {
        String invalidData = "{ \"accountType\": \"\", \"agencyId\": \"" + agency.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/account/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidData))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createAccount_WithExistingAccount() throws Exception {
        String existingAccountJson = "{ \"accountType\": \"CURRENT\", \"agencyId\": \"" + agency.getId() + "\", \"clientId\": \"" + client.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(existingAccountJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", is("Account type already exists")));
    }

    @Test
    void updateAccount_WithNonExistentId() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        String updateJson = "{ \"accountType\": \"SAVINGS\", \"agencyId\": \"" + agency.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/account/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAccount_WithNonExistentId() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/account/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
