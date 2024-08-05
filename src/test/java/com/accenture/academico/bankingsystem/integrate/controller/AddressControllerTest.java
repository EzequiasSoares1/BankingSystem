package com.accenture.academico.bankingsystem.integrate.controller;

import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.dto.address.AddressRequestDTO;
import com.accenture.academico.bankingsystem.repositories.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class AddressControllerTest implements ConfigSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepository addressRepository;

    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address(UUID.randomUUID(), "58700-010", "872", "Rua do Prado", "Centro");
        address = addressRepository.save(address);
    }

    @Test
    void getAllAddress() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cep", is("58700-010")));
    }

    @Test
    void create() throws Exception {
        AddressRequestDTO request = new AddressRequestDTO("58703-000", "877", "Rua do Prado", "Liberdade");

        mockMvc.perform(MockMvcRequestBuilders.post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cep\": \"58703-000\", \"number\": \"877\", \"street\": \"Rua do Prado\", \"district\": \"Liberdade\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cep", is(request.cep())))
                .andExpect(jsonPath("$.number", is(request.number())))
                .andExpect(jsonPath("$.street", is(request.street())))
                .andExpect(jsonPath("$.district", is(request.district())));
    }

    @Test
    void update() throws Exception {
        AddressRequestDTO request = new AddressRequestDTO("58703-001", "878", "Rua Nova", "Liberdade Nova");

        mockMvc.perform(MockMvcRequestBuilders.put("/address/" + address.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cep\": \"58703-001\", \"number\": \"878\", \"street\": \"Rua Nova\", \"district\": \"Liberdade Nova\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep", is(request.cep())))
                .andExpect(jsonPath("$.number", is(request.number())))
                .andExpect(jsonPath("$.street", is(request.street())))
                .andExpect(jsonPath("$.district", is(request.district())));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/address/" + address.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void createWithInvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cep\": \"\", \"number\": \"\", \"street\": \"\", \"district\": \"\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateNonExistentAddress() throws Exception {
        AddressRequestDTO request = new AddressRequestDTO("58703-001", "878", "Rua Nova", "Liberdade Nova");

        mockMvc.perform(MockMvcRequestBuilders.put("/address/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cep\": \"58703-001\", \"number\": \"878\", \"street\": \"Rua Nova\", \"district\": \"Liberdade Nova\" }"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNonExistentAddress() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/address/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getAddressById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/address/" + address.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep", is(address.getCep())))
                .andExpect(jsonPath("$.number", is(address.getNumber())))
                .andExpect(jsonPath("$.street", is(address.getStreet())))
                .andExpect(jsonPath("$.district", is(address.getDistrict())));
    }

    @Test
    void getNonExistentAddress() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/address/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Address not found with ID")));
    }

}
