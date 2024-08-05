package com.accenture.academico.bankingsystem.integrate.controller;

import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.dto.UserDTO;
import com.accenture.academico.bankingsystem.services.general.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest implements ConfigSpringTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    private static ObjectMapper objectMapper;
    private static UserDTO userDTO;
    private static UUID userId;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        userDTO = new UserDTO(UUID.randomUUID(), "john.odoe@example.com", "password", "ADMIN");

        userId = userService.saveUser(userDTO).id();
    }

    @Test
    @Order(1)
    @DisplayName("CreateUserTest")
    void createUserTest() throws Exception {
        UserDTO userDTO1 = new UserDTO(UUID.randomUUID(), "john.doe@example.com", "password", "ADMIN");
        String json = objectMapper.writeValueAsString(userDTO1);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        UserDTO createdUserDTO = objectMapper.readValue(jsonResponse, UserDTO.class);

        Assertions.assertNotNull(createdUserDTO);
        Assertions.assertEquals(userDTO1.email(), createdUserDTO.email());
    }

    @Test
    @Order(2)
    @DisplayName("GetAllUsersTest")
    void getAllUsersTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<UserDTO> userDTOs = objectMapper.readValue(jsonResponse, new TypeReference<List<UserDTO>>() {});

        Assertions.assertTrue(userDTOs.size() > 0);
    }

    @Test
    @Order(3)
    @DisplayName("GetUserByIdTest")
    void getUserByIdTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        UserDTO fetchedUserDTO = objectMapper.readValue(jsonResponse, UserDTO.class);

        Assertions.assertNotNull(fetchedUserDTO);
        Assertions.assertEquals(userId, fetchedUserDTO.id());
    }

    @Test
    @Order(4)
    @DisplayName("GetUserByEmailTest")
    void getUserByEmailTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/email/" + userDTO.email())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        UserDTO fetchedUserDTO = objectMapper.readValue(jsonResponse, UserDTO.class);

        Assertions.assertNotNull(fetchedUserDTO);
        Assertions.assertEquals(userDTO.email(), fetchedUserDTO.email());
    }

    @Test
    @Order(5)
    @DisplayName("UpdateUserTest")
    void updateUserTest() throws Exception {
        String json = objectMapper.writeValueAsString(userDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        UserDTO updatedUserDTO = objectMapper.readValue(jsonResponse, UserDTO.class);

        Assertions.assertNotNull(updatedUserDTO);
        Assertions.assertEquals("john.odoe@example.com", updatedUserDTO.email());
    }

    @Test
    @Order(6)
    @DisplayName("DeleteUserTest")
    void deleteUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(7)
    @DisplayName("DeleteUserErrorTest")
    void deleteUserErrorTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(8)
    @DisplayName("CreateUserInvalidEmailTest")
    void createUserInvalidEmailTest() throws Exception {
        UserDTO invalidUserDTO = new UserDTO(UUID.randomUUID(), "invalid-email", "password", "ADMIN");
        String json = objectMapper.writeValueAsString(invalidUserDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Order(9)
    @DisplayName("GetUserByIdNotFoundTest")
    void getUserByIdNotFoundTest() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get("/user/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(10)
    @DisplayName("GetUserByEmailNotFoundTest")
    void getUserByEmailNotFoundTest() throws Exception {
        String nonExistentEmail = "nonexistent@example.com";

        mockMvc.perform(MockMvcRequestBuilders.get("/user/email/" + nonExistentEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(11)
    @DisplayName("UpdateUserInvalidDataTest")
    void updateUserInvalidDataTest() throws Exception {
        UserDTO invalidUserDTO = new UserDTO(UUID.randomUUID(), "invalid-email", "password", "ADMIN");
        String json = objectMapper.writeValueAsString(invalidUserDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
