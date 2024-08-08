package com.accenture.academico.bankingsystem.integrate.services;

import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.client.ClientRequestDTO;
import com.accenture.academico.bankingsystem.exceptions.ConflictException;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.repositories.AddressRepository;
import com.accenture.academico.bankingsystem.repositories.ClientRepository;
import com.accenture.academico.bankingsystem.repositories.UserRepository;
import com.accenture.academico.bankingsystem.services.AddressService;
import com.accenture.academico.bankingsystem.services.ClientService;
import com.accenture.academico.bankingsystem.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServiceTest implements ConfigSpringTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private Address address;
    private Client client;
    private User user;

    @BeforeEach
    void setUp() {
        address = new Address(UUID.randomUUID(), "58700-010", "872", "Rua do Prado", "Centro");
        address = addressRepository.save(address);

        user = new User();
        user.setEmail("testUser@email.com");
        user.setPassword("password");
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);

        SecurityContextHolder.setContext(new SecurityContextImpl(
                new UsernamePasswordAuthenticationToken(user, "password", user.getAuthorities())
        ));

        client = new Client();
        client.setName("Cliente Teste");
        client.setCpf("12345678901");
        client.setTelephone("123456789");
        client.setAddress(address);
        client.setUser(user);
        client = clientRepository.save(client);
    }

    @Test
    void getAllClients() {
        var clients = clientService.getAllClients();
        assertFalse(clients.isEmpty());
    }

    @Test
    void getClientById() {
        var foundClient = clientService.getClientById(client.getId());
        assertNotNull(foundClient);
        assertEquals(client.getName(), foundClient.name());
    }

    @Test
    void getClientById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> {
            clientService.getClientById(nonExistentId);
        });
    }

    @Test
    void createClient() {
        Address addressRequest = new Address(UUID.randomUUID(), "58703-000", "877", "Rua do Prado", "Liberdade");
        addressRequest = addressRepository.save(addressRequest);
        ClientRequestDTO request = new ClientRequestDTO("Cliente Novo", "98765432100", "987654321", addressRequest.getId());

        var createdClient = clientService.createClient(request);

        assertNotNull(createdClient);
        assertEquals(request.name(), createdClient.name());
        assertEquals(request.cpf(), createdClient.cpf());
        assertEquals(request.telephone(), createdClient.telephone());
    }

    @Test
    void createClient_Conflict() {
        ClientRequestDTO request = new ClientRequestDTO(client.getName(), client.getCpf(), client.getTelephone(), address.getId());

        assertThrows(ConflictException.class, () -> {
            clientService.createClient(request);
        });
    }

    @Test
    void createClient_InvalidAddress() {
        UUID invalidAddressId = UUID.randomUUID();
        ClientRequestDTO request = new ClientRequestDTO("Cliente Novo", "98765432100", "987654321", invalidAddressId);

        assertThrows(NotFoundException.class, () -> {
            clientService.createClient(request);
        });
    }

    @Test
    void updateClient() {
        ClientRequestDTO updateRequest = new ClientRequestDTO("Cliente Atualizado", "98765432100", "987654321", address.getId());

        var updatedClient = clientService.updateClient(client.getId(), updateRequest);

        assertNotNull(updatedClient);
        assertEquals(updateRequest.name(), updatedClient.name());
        assertEquals(updateRequest.cpf(), updatedClient.cpf());
        assertEquals(updateRequest.telephone(), updatedClient.telephone());
    }

    @Test
    void updateNonExistentClient() {
        UUID nonExistentId = UUID.randomUUID();
        ClientRequestDTO updateRequest = new ClientRequestDTO("Cliente Atualizado", "98765432100", "987654321", address.getId());

        assertThrows(NotFoundException.class, () -> {
            clientService.updateClient(nonExistentId, updateRequest);
        });
    }

    @Test
    void updateClient_InvalidAddress() {
        UUID invalidAddressId = UUID.randomUUID();
        ClientRequestDTO updateRequest = new ClientRequestDTO("Cliente Atualizado", "98765432100", "987654321", invalidAddressId);

        assertThrows(NotFoundException.class, () -> {
            clientService.updateClient(client.getId(), updateRequest);
        });
    }

    @Test
    void deleteClient() {
        clientService.deleteClient(client.getId());

        assertThrows(NotFoundException.class, () -> {
            clientService.getClientById(client.getId());
        });
    }

    @Test
    void deleteNonExistentClient() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(NotFoundException.class, () -> {
            clientService.deleteClient(nonExistentId);
        });
    }

    @Test
    void findById() {
        var foundClient = clientService.getClientById(client.getId());
        assertNotNull(foundClient);
        assertEquals(client.getName(), foundClient.name());
    }

    @Test
    void findById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(NotFoundException.class, () -> {
            clientService.getClientById(nonExistentId);
        });
    }
}
