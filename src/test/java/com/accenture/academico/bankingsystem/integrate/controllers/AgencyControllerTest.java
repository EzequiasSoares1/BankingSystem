package com.accenture.academico.bankingsystem.integrate.controllers;
import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.agency.AgencyRequestDTO;
import com.accenture.academico.bankingsystem.dtos.agency.AgencyDTO;
import com.accenture.academico.bankingsystem.services.AddressService;
import com.accenture.academico.bankingsystem.services.AgencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class AgencyControllerTest implements ConfigSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private AddressService addressService;

    private Address address;
    private AgencyDTO agency;

    @BeforeEach
    void setUp() {
        address = new Address(UUID.randomUUID(), "58700-010", "872", "Rua do Prado", "Centro");
        address = addressService.create(new Address(UUID.randomUUID(),"58700-010", "872", "Rua do Prado", "Centro"));

        AgencyRequestDTO requestDTO = new AgencyRequestDTO(UUID.randomUUID(), "Agência Centro", "123456789","436234534", address);
        agency = agencyService.createAgency(requestDTO);

        User user = new User();
        user.setEmail("testUser@email.com");
        user.setPassword("password");
        user.setRole(Role.ADMIN);

        Authentication mockAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(mockAuthentication.isAuthenticated()).thenReturn(true);
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
    }

    @Test
    void getAllAgencies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAgencyById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agency/" + agency.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Agência Centro")));
    }

    @Test
    void createAgency() throws Exception {
        Address addressRequest = new Address(UUID.randomUUID(),"58703-000", "877", "Rua do Prado", "Liberdade");
        Address address = addressService.create(addressRequest);

        String newAgencyJson = "{ \"number\": \"002\", \"name\": \"Agência Novo\", \"telephone\": \"987654321\", \"address\": { \"cep\": \"58703-000\", \"number\": \"877\", \"street\": \"Rua do Prado\", \"district\": \"Liberdade\" } }";

        mockMvc.perform(MockMvcRequestBuilders.post("/agency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newAgencyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is("002")))
                .andExpect(jsonPath("$.name", is("Agência Novo")))
                .andExpect(jsonPath("$.telephone", is("987654321")));
    }

    @Test
    void updateAgency() throws Exception {
        String updatedAgencyJson = "{\"id\":\""+ agency.id()+"\",\"name\": \"Agência Atualizada\", \"telephone\": \"987654321\", \"number\": \"003\", \"address_id\": \"" + address.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/agency/" + agency.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAgencyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Agência Atualizada")))
                .andExpect(jsonPath("$.telephone", is("987654321")))
                .andExpect(jsonPath("$.number", is("003")));
    }

    @Test
    void deleteAgency() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/agency/" + agency.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void createAgency_WithInvalidData() throws Exception {
        String invalidData = "{ \"number\": \"\", \"name\": \"\", \"telephone\": \"\", \"address\": { \"cep\": \"\", \"number\": \"\", \"street\": \"\", \"district\": \"\" } }";

        mockMvc.perform(MockMvcRequestBuilders.post("/agency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAgency_WithInvalidData() throws Exception {
        String invalidData = "{ \"name\": \"\", \"telephone\": \"\", \"number\": \"\", \"address_id\": \"" + address.getId() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/agency/" + agency.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAgency_WithExistingNumber() throws Exception {
        String existingNumberJson = "{ \"number\": \"436234534\", \"name\": \"Agência Centro\", \"telephone\": \"987654321\", \"address\": { \"cep\": \"58703-000\", \"number\": \"436234534\", \"street\": \"Rua do Prado\", \"district\": \"Liberdade\" } }";

        mockMvc.perform(MockMvcRequestBuilders.post("/agency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(existingNumberJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", is("Agency already exists")));
    }

    @Test
    void updateAgency_WithNonExistentId() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        String updateJson = "{ \"id\": \""+nonExistentId+"\",\"name\": \"Agência Centro\", \"telephone\": \"987654321\", \"number\": \"436234534\", \"address_id\": \"" + UUID.randomUUID() + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/agency/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAgency_WithNonExistentId() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/agency/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
