package com.accenture.academico.bankingsystem.unit.security;

import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dto.ResponseToken;
import com.accenture.academico.bankingsystem.repositories.UserRepository;
import com.accenture.academico.bankingsystem.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

public class TokenServiceTest implements ConfigSpringTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    void testGenerateToken() {
        User user = createUser("test@example.com", "password");
        ResponseToken token = tokenService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.token().isEmpty());
        assertFalse(token.tokenRefresh().isEmpty());
    }

    @Test
    @Transactional
    void testValidateToken() {
        User user = createUser("test@example.com", "password");
        ResponseToken token = tokenService.generateToken(user);
        assertNotNull(tokenService.validateToken(token.token()));
    }

    @Test
    @Transactional
    void testIsValidToken() {
        User user = createUser("test@example.com", "password");
        ResponseToken tokenResponse = tokenService.generateToken(user);

        assertTrue(tokenService.isValidToken(tokenResponse.token()));
    }

    @Test
    @Transactional
    void testGenNewToken() {
        User user = createUser("test@example.com", "password");
        ResponseToken tokenResponse = tokenService.generateToken(user);
        assertNotNull(tokenResponse.tokenRefresh(), tokenResponse.token());
    }

    @Test
    @Transactional
    void testGenerateToken_UserNotFound() {
        User user = null;
        assertThrows(RuntimeException.class, () -> tokenService.generateToken(user));
    }

    @Test
    void testIsValidToken_InvalidToken() {
        String invalidToken = "invalid-token";
        assertFalse(tokenService.isValidToken(invalidToken));
    }

    @Test
    void testGenNewToken_InvalidToken() {
        String invalidToken = "invalid-token";
        assertThrows(RuntimeException.class, () -> tokenService.genNewToken(invalidToken));
    }

    @Test
    void testGenNewToken_UserNotFound() {
        String tokenResponse = "valid-token-response";
        assertThrows(RuntimeException.class, () -> tokenService.genNewToken(tokenResponse));
    }


    private User createUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
}
