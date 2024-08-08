package com.accenture.academico.bankingsystem.integrate.controllers;

import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.client.ClientRequestDTO;
import com.accenture.academico.bankingsystem.dtos.client.ClientResponseDTO;
import com.accenture.academico.bankingsystem.exceptions.NotAuthorizeException;
import com.accenture.academico.bankingsystem.middlewares.UserTools;
import com.accenture.academico.bankingsystem.repositories.UserRepository;
import com.accenture.academico.bankingsystem.services.AddressService;
import com.accenture.academico.bankingsystem.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
public class ClientControllerTest implements ConfigSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressService addressService;

    @MockBean  // Add @MockBean annotation
    private UserTools userTools;

    private Address address;
    private ClientResponseDTO client;
    private User user;

    @BeforeEach
    void setUp() {
        // Criação de um endereço
        address = addressService.create(new Address(UUID.randomUUID(), "58700-010", "872", "Rua do Prado", "Centro"));

        ClientRequestDTO requestDTO = new ClientRequestDTO("Cliente Teste", "12345678900", "123456789", address.getId());
        client = clientService.createClient(requestDTO);

        user = new User();
        user.setId(UUID.randomUUID()); // Ensure that the ID is set
        user.setEmail("testUser@email.com");
        user.setPassword("password");
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }


    @Test
    void getAllClients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/client")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getClientById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/client/" + client.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cliente Teste")));
    }

    @Test
    void createClient() throws Exception {
        String newClientJson = "{ \"name\": \"Cliente Novo\", \"cpf\": \"98765432100\", \"telephone\": \"987654321\", \"address_id\": \"" + address.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newClientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Cliente Novo")))
                .andExpect(jsonPath("$.cpf", is("98765432100")))
                .andExpect(jsonPath("$.telephone", is("987654321")));
    }

    @Test
    void updateClient() throws Exception {
        String updatedClientJson = "{ \"name\": \"Cliente Atualizado\", \"cpf\": \"98765432100\", \"telephone\": \"987654321\", \"address_id\": \"" + address.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/client/" + client.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cliente Atualizado")))
                .andExpect(jsonPath("$.cpf", is("98765432100")))
                .andExpect(jsonPath("$.telephone", is("987654321")));
    }

    @Test
    void deleteClient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/client/" + client.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void createClient_WithInvalidData() throws Exception {
        String invalidData = "{ \"name\": \"\", \"cpf\": \"\", \"telephone\": \"\", \"address_id\": \"" + address.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateClient_WithInvalidData() throws Exception {
        String invalidData = "{ \"name\": \"\", \"cpf\": \"\", \"telephone\": \"\", \"address_id\": \"" + address.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/client/" + client.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createClient_WithExistingCpf() throws Exception {
        String existingCpfJson = "{ \"name\": \"Cliente Duplicado\", \"cpf\": \"12345678900\", \"telephone\": \"987654321\", \"address_id\": \"" + address.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(existingCpfJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", is("Client already exists")));
    }

    @Test
    void updateClient_WithNonExistentId() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        String updateJson = "{ \"name\": \"Cliente Inexistente\", \"cpf\": \"98765432100\", \"telephone\": \"987654321\", \"address_id\": \"" + UUID.randomUUID() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/client/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteClient_WithNonExistentId() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/client/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
