package com.accenture.academico.bankingsystem.integrate.controllers;
import com.accenture.academico.bankingsystem.config.ConfigIntegrateSpringTest;
import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.PixKeyType;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.pix_key.PixKey;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@AutoConfigureMockMvc(addFilters = false)
public class PixKeyControllerTestIntegrate implements ConfigIntegrateSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PixKeyRepository pixKeyRepository;

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
    private PixKey pixKey;

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

        pixKey = new PixKey();
        pixKey.setId(UUID.randomUUID());
        pixKey.setKeyType(PixKeyType.TELEPHONE);
        pixKey.setKeyValue("834826637");
        pixKey.setAccount(account);

        pixKey = pixKeyRepository.save(pixKey);
    }

    @Test
    void getAllPixKeyByAccountId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pix-key/account/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(pixKey.getId().toString())))
                .andExpect(jsonPath("$[0].keyType", is(pixKey.getKeyType().toString())))
                .andExpect(jsonPath("$[0].keyValue", is(pixKey.getKeyValue().toString())));
    }

    @Test
    void getPixKeyById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pix-key/" + pixKey.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(pixKey.getId().toString())))
                .andExpect(jsonPath("$.keyType", is(pixKey.getKeyType().toString())))
                .andExpect(jsonPath("$.keyValue", is(pixKey.getKeyValue().toString())));
    }

    @Test
    void createPixKey() throws Exception {
        String newPixKeyJson = "{ \"keyType\": \"CPF\", \"keyValue\": \"98765432101\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/pix-key/account/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPixKeyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.keyType", is("CPF")))
                .andExpect(jsonPath("$.keyValue", is("98765432101")));
    }

    @Test
    void deletePixKey() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/pix-key/" + pixKey.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/pix-key/" + pixKey.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPixKey_WithInvalidData() throws Exception {
        String invalidData = "{ \"keyType\": \"CPF\", \"keyValue\": null }";

        mockMvc.perform(MockMvcRequestBuilders.post("/pix-key/account/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePixKey_WithNonExistentId() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/pix-key/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
