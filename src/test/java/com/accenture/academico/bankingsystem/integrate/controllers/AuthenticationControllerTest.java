package com.accenture.academico.bankingsystem.integrate.controllers;

import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.dtos.user.AuthenticationDTO;
import com.accenture.academico.bankingsystem.dtos.user.ResponseTokenDTO;
import com.accenture.academico.bankingsystem.dtos.user.UserDTO;
import com.accenture.academico.bankingsystem.services.AuthenticationService;
import com.accenture.academico.bankingsystem.services.UserService;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTest implements ConfigSpringTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    private static ObjectMapper objectMapper;
    private static String validToken;
    private static String validTokenRefresh;

    private static UserDTO testUser;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Create a test user and get a valid token
        testUser = new UserDTO(UUID.randomUUID(), "user@example.com", "password", "ADMIN");
        userService.saveUser(testUser);

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(testUser.email(), testUser.password());
        ResponseTokenDTO responseTokenDTO = authenticationService.login(authenticationDTO);
        validToken = responseTokenDTO.token();
        validTokenRefresh = responseTokenDTO.tokenRefresh();
    }

    @Test
    @Order(1)
    @DisplayName("LoginSuccessTest")
    void loginSuccessTest() throws Exception {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(testUser.email(), testUser.password());
        String json = objectMapper.writeValueAsString(authenticationDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(jsonResponse);
    }

    @Test
    @Order(2)
    @DisplayName("LoginFailureTest")
    void loginFailureTest() throws Exception {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(testUser.email(), "wrongpassword");
        String json = objectMapper.writeValueAsString(authenticationDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Order(3)
    @DisplayName("IsValidTokenSuccessTest")
    void isValidTokenSuccessTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/isValidToken")
                        .param("token", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(4)
    @DisplayName("IsValidTokenFailureTest")
    void isValidTokenFailureTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/isValidToken")
                        .param("token", "invalid-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Order(5)
    @DisplayName("TokenRefreshSuccessTest")
    void tokenRefreshSuccessTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refreshToken")
                        .param("token", validTokenRefresh)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(6)
    @DisplayName("TokenRefreshFailureTest")
    void tokenRefreshFailureTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refreshToken")
                        .param("token", "invalid-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
