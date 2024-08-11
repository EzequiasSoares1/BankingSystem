package com.accenture.academico.bankingsystem.unit.mappers;

import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.client.ClientResponseDTO;
import com.accenture.academico.bankingsystem.mappers.client.ClientMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientMapperTest {
    @Test
    void testConvertToClientResponseDTO(){
        User user = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        Address address = new Address(UUID.randomUUID(), "58700010", "888", "Rua Teste", "Centro", LocalDateTime.now(), LocalDateTime.now());

        Client client = new Client(UUID.randomUUID(), "Teste", "11111111", "8388776655", address, user, LocalDateTime.now(), LocalDateTime.now());

        ClientResponseDTO clientResponseDTO = ClientMapper.convertToClientResponseDTO(client);

        assertNotNull(clientResponseDTO);
        assertEquals(clientResponseDTO.id(), client.getId());
        assertEquals(clientResponseDTO.name(), client.getName());
        assertEquals(clientResponseDTO.cpf(), client.getCpf());
        assertEquals(clientResponseDTO.telephone(), client.getTelephone());
        assertEquals(clientResponseDTO.address_id(), client.getAddress().getId());
        assertEquals(clientResponseDTO.user_id(), client.getUser().getId());
    }

    @Test
    void testConvertToClientResponseDTOList(){
        User user = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        Address address = new Address(UUID.randomUUID(), "58700010", "888", "Rua Teste", "Centro", LocalDateTime.now(), LocalDateTime.now());

        Client client1 = new Client(UUID.randomUUID(), "Teste1", "11111111", "8388776655", address, user, LocalDateTime.now(), LocalDateTime.now());
        Client client2 = new Client(UUID.randomUUID(), "Teste2", "22222222", "8388776655", address, user, LocalDateTime.now(), LocalDateTime.now());

        List<Client> clientList = List.of(client1, client2);

        List<ClientResponseDTO> clientResponseDTOList = ClientMapper.convertToClientResponseDTOList(clientList);

        assertNotEquals(clientResponseDTOList.size(), 0);
        assertEquals(clientResponseDTOList.get(0).id(), clientList.get(0).getId());
        assertEquals(clientResponseDTOList.get(1).id(), clientList.get(1).getId());
        assertEquals(clientResponseDTOList.get(0).name(), clientList.get(0).getName());
        assertEquals(clientResponseDTOList.get(1).name(), clientList.get(1).getName());
        assertEquals(clientResponseDTOList.get(0).cpf(), clientList.get(0).getCpf());
        assertEquals(clientResponseDTOList.get(1).cpf(), clientList.get(1).getCpf());
        assertEquals(clientResponseDTOList.get(0).telephone(), clientList.get(0).getTelephone());
        assertEquals(clientResponseDTOList.get(1).telephone(), clientList.get(1).getTelephone());
        assertEquals(clientResponseDTOList.get(0).address_id(), clientList.get(0).getAddress().getId());
        assertEquals(clientResponseDTOList.get(1).address_id(), clientList.get(1).getAddress().getId());
        assertEquals(clientResponseDTOList.get(0).user_id(), clientList.get(0).getUser().getId());
        assertEquals(clientResponseDTOList.get(1).user_id(), clientList.get(1).getUser().getId());
    }
}
