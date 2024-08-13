package com.accenture.academico.bankingsystem.unit.domain;

import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @Test
    void testClientBuilder() {
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        String cpf = "12345678901";
        String telephone = "555-1234";
        Address address = new Address();
        User user = new User();

        Client client = new Client(id, name, cpf, telephone, address, user);

        assertEquals(id, client.getId());
        assertEquals(name, client.getName());
        assertEquals(cpf, client.getCpf());
        assertEquals(telephone, client.getTelephone());
        assertEquals(address, client.getAddress());
        assertEquals(user, client.getUser());
    }

    @Test
    void testDefaultConstructor() {
        Client client = new Client();

        assertNotNull(client);
    }

    @Test
    void testSettersAndGetters() {
        Client client = new Client();
        UUID id = UUID.randomUUID();
        String name = "Jane Smith";
        String cpf = "10987654321";
        String telephone = "555-5678";
        Address address = new Address();
        User user = new User();

        client.setId(id);
        client.setName(name);
        client.setCpf(cpf);
        client.setTelephone(telephone);
        client.setAddress(address);
        client.setUser(user);

        assertEquals(id, client.getId());
        assertEquals(name, client.getName());
        assertEquals(cpf, client.getCpf());
        assertEquals(telephone, client.getTelephone());
        assertEquals(address, client.getAddress());
        assertEquals(user, client.getUser());
    }

    @Test
    void testPrePersist() {
        Client client = new Client();
        client.prePersist();

        assertNotNull(client.getCreatedDate());
        assertTrue(client.getCreatedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPreUpdate() {
        Client client = new Client();
        client.prePersist();
        LocalDateTime initialUpdatedDate = client.getUpdatedDate();

        client.preUpdate();

        assertEquals(initialUpdatedDate, client.getUpdatedDate());
        assertTrue(client.getUpdatedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
