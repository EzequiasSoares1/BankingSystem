package com.accenture.academico.bankingsystem.unit.security;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.repositories.UserRepository;
import com.accenture.academico.bankingsystem.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc

public class SecurityFilterIntegrationTestIntegrate {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private String validToken;

    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password123");

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword(encodedPassword);
        testUser.setRole(Role.ADMIN);
        testUser = userRepository.save(testUser);


        // Gera um token válido para o usuário de teste
        validToken = tokenService.generateToken(testUser).token();
    }

    @Test

    void testAccessSecuredEndpoint_WithValidToken() throws Exception {
        mockMvc.perform(get("/user")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }


    @Test
    void testAccessSecuredEndpoint_WithoutToken() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
    }
}
