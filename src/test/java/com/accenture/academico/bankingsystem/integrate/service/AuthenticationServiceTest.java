package com.accenture.academico.bankingsystem.integrate.service;

import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dto.AuthenticationDTO;
import com.accenture.academico.bankingsystem.dto.ResponseToken;
import com.accenture.academico.bankingsystem.exception.InternalLogicException;
import com.accenture.academico.bankingsystem.repositories.UserRepository;
import com.accenture.academico.bankingsystem.security.TokenService;
import com.accenture.academico.bankingsystem.services.general.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class AuthenticationServiceTest {

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
        testUser = User.builder()
                .email("test@example.com")
                .password("password123")
                .build();
        userRepository.save(testUser);
    }

    @Test
    void testLoginSuccess() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("test@example.com", "password123");
        Authentication authentication = new UsernamePasswordAuthenticationToken(testUser.getEmail(), testUser.getPassword());

        Authentication authResult = authenticationManager.authenticate(authentication);
        assertEquals(testUser.getEmail(), ((User) authResult.getPrincipal()).getEmail());

        ResponseToken responseToken = authenticationService.login(authenticationDTO);
        assertNotNull(responseToken);
        assertNotNull(responseToken.token());
        assertNotNull(responseToken.tokenRefresh());
    }

    @Test
    void testLoginFailure() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("test@example.com", "wrongpassword");

        assertThrows(InternalLogicException.class, () -> authenticationService.login(authenticationDTO));
    }

    @Test
    void testValidateTokenSuccess() {
        String token = tokenService.generateToken(testUser).token();

        authenticationService.validateToken(token);
    }

    @Test
    void testValidateTokenFailure() {
        String token = "invalidToken";

        assertThrows(InternalLogicException.class, () -> authenticationService.validateToken(token));
    }

    @Test
    void testTokenRefresh() {
        String oldToken = tokenService.generateToken(testUser).tokenRefresh();

        ResponseToken responseToken = authenticationService.tokenRefresh(oldToken);

        assertNotNull(responseToken);
        assertNotNull(responseToken.token());
        assertNotNull(responseToken.tokenRefresh());
    }
}
