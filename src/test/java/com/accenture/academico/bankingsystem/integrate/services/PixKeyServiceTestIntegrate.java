package com.accenture.academico.bankingsystem.integrate.services;
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
import com.accenture.academico.bankingsystem.dtos.pix_key.PixKeyRequestDTO;
import com.accenture.academico.bankingsystem.dtos.pix_key.PixKeyResponseDTO;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.repositories.*;
import com.accenture.academico.bankingsystem.services.AccountService;
import com.accenture.academico.bankingsystem.services.PixKeyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PixKeyServiceTestIntegrate implements ConfigIntegrateSpringTest {

    @Autowired
    private PixKeyService pixKeyService;

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
    void createPixKey() {
        PixKeyRequestDTO request = new PixKeyRequestDTO(PixKeyType.TELEPHONE, "123456789");
        PixKeyResponseDTO response = pixKeyService.createPixKey(account.getId(), request);

        assertNotNull(response);
        assertEquals(request.keyType(), response.keyType());
        assertEquals(request.keyValue(), response.keyValue());
    }

    @Test
    void createPixKey_NotFound() {
        PixKeyRequestDTO request =new PixKeyRequestDTO(PixKeyType.TELEPHONE, "123456789");

        assertThrows(NotFoundException.class, () -> {
            pixKeyService.createPixKey(UUID.randomUUID(), request);
        });
    }

    @Test
    void getAllPixKeyByAccountId() {
        List<PixKeyResponseDTO> pixKeys = pixKeyService.getAllPixKeyByAccountId(account.getId());

        assertNotNull(pixKeys);
        assertFalse(pixKeys.isEmpty());
        assertEquals(pixKey.getKeyValue(), pixKeys.get(0).keyValue());
    }

    @Test
    void getAllPixKeyByAccountId_NoKeys() {
        List<PixKeyResponseDTO> pixKeys = pixKeyService.getAllPixKeyByAccountId(UUID.randomUUID());

        assertNotNull(pixKeys);
        assertTrue(pixKeys.isEmpty());
    }

    @Test
    void getPixKeyById() {
        PixKeyResponseDTO response = pixKeyService.getPixKeyById(pixKey.getId());

        assertNotNull(response);
        assertEquals(pixKey.getKeyType(), response.keyType());
        assertEquals(pixKey.getKeyValue(), response.keyValue());
    }

    @Test
    void getPixKeyById_NotFound() {
        assertThrows(NotFoundException.class, () -> {
            pixKeyService.getPixKeyById(UUID.randomUUID());
        });
    }

    @Test
    void deletePixKey() {
        pixKeyService.deletePixKey(pixKey.getId());

        assertThrows(NotFoundException.class, () -> {
            pixKeyService.getPixKeyById(pixKey.getId());
        });
    }

    @Test
    void deletePixKey_NotFound() {
        assertThrows(NotFoundException.class, () -> {
            pixKeyService.deletePixKey(UUID.randomUUID());
        });
    }
}
