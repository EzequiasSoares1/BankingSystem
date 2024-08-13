package com.accenture.academico.bankingsystem.integrate.services;

import com.accenture.academico.bankingsystem.config.ConfigIntegrateSpringTest;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.user.AuthenticationRequestDTO;
import com.accenture.academico.bankingsystem.dtos.user.TokenResponseDTO;
import com.accenture.academico.bankingsystem.exceptions.InternalLogicException;
import com.accenture.academico.bankingsystem.repositories.UserRepository;
import com.accenture.academico.bankingsystem.security.TokenService;
import com.accenture.academico.bankingsystem.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationServiceTestIntegrate implements ConfigIntegrateSpringTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password123");

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword(encodedPassword);
        testUser.setRole(Role.ADMIN);
        userRepository.save(testUser);
    }

    @Test
    @Order(1)
    void testLoginSuccess() {
        AuthenticationRequestDTO authenticationDTO = new AuthenticationRequestDTO("test@example.com", "password123");
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.password());

        Authentication authResult = authenticationManager.authenticate(authentication);
        assertEquals(testUser.getEmail(), ((User) authResult.getPrincipal()).getEmail());

        TokenResponseDTO responseTokenDTO = authenticationService.login(authenticationDTO);
        assertNotNull(responseTokenDTO);
        assertNotNull(responseTokenDTO.token());
        assertNotNull(responseTokenDTO.tokenRefresh());
    }

    @Test
    @Order(2)
    void testLoginFailure() {
        AuthenticationRequestDTO authenticationDTO = new AuthenticationRequestDTO("test@example.com", "wrongpassword");

        assertThrows(InternalLogicException.class, () -> authenticationService.login(authenticationDTO));
    }

    @Test
    @Order(3)
    void testValidateTokenSuccess() {
        String token = tokenService.generateToken(testUser).token();

        authenticationService.validateToken(token);
    }

    @Test
    @Order(4)
    void testValidateTokenFailure() {
        String token = "invalidToken";

        assertThrows(InternalLogicException.class, () -> authenticationService.validateToken(token));
    }

    @Test
    @Order(5)
    void testTokenRefresh() {
        String oldToken = tokenService.generateToken(testUser).tokenRefresh();

        TokenResponseDTO responseTokenDTO = authenticationService.tokenRefresh(oldToken);

        assertNotNull(responseTokenDTO);
        assertNotNull(responseTokenDTO.token());
        assertNotNull(responseTokenDTO.tokenRefresh());
    }
}
