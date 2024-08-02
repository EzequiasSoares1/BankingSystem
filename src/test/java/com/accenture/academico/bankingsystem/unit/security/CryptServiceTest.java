package com.accenture.academico.bankingsystem.unit.security;

import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.security.CryptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class CryptServiceTest implements ConfigSpringTest {

    @Autowired
    private CryptService cryptService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testEncodePassword() {
        String password = "testPassword";

        String encodedPassword = cryptService.encodePassword(password);

        assertNotNull(encodedPassword);
        assertNotEquals(password, encodedPassword);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

    @Test
    void testMatchesPassword() {
        String password = "testPassword";
        String encodedPassword = passwordEncoder.encode(password);

        assertTrue(cryptService.matchesPassword(password, encodedPassword)); // A senha codificada deve corresponder à senha original
    }
}
