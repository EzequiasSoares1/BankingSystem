package com.accenture.academico.bankingsystem.unit.domain;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    void testUserBuilder() {
        UUID id = UUID.randomUUID();
        String email = "test@example.com";
        Role role = Role.ADMIN;
        String password = "password123";

        User user = User.builder()
                .id(id)
                .email(email)
                .role(role)
                .password(password)
                .build();

        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(role, user.getRole());
        assertEquals(password, user.getPassword());
    }

    @Test
    void testUserAuthorities() {
        User adminUser = User.builder()
                .email("admin@example.com")
                .role(Role.ADMIN)
                .password("admin123")
                .build();

        assertEquals(2, adminUser.getAuthorities().size());
        assertTrue(adminUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
        assertTrue(adminUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("CLIENT")));

        User defaultUser = User.builder()
                .email("user@example.com")
                .role(Role.CLIENT)
                .password("user123")
                .build();

        assertEquals(1, defaultUser.getAuthorities().size());
        assertTrue(defaultUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("CLIENT")));
    }

    @Test
    void testUserAccountStatus() {
        // Test isEnabled method
        User enabledUser = User.builder()
                .email("enabled@example.com")
                .role(Role.CLIENT)
                .password("enabled123")
                .build();

        assertTrue(enabledUser.isEnabled());
        assertTrue(enabledUser.isAccountNonExpired());
        assertTrue(enabledUser.isAccountNonLocked());
        assertTrue(enabledUser.isCredentialsNonExpired());
    }

}
