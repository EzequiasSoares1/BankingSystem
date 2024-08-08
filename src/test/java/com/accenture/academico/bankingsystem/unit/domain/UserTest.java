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


        User testUser = new User();
        testUser.setId(id);
        testUser.setEmail(email);
        testUser.setPassword(password);
        testUser.setRole(role);

        assertEquals(id, testUser.getId());
        assertEquals(email, testUser.getEmail());
        assertEquals(role, testUser.getRole());
        assertEquals(password, testUser.getPassword());
    }

    @Test
    void testUserAuthorities() {
        User adminUser = new User();
        adminUser.setEmail("test@example.com");
        adminUser.setPassword("Password");
        adminUser.setRole(Role.ADMIN);

        assertEquals(2, adminUser.getAuthorities().size());
        assertTrue(adminUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
        assertTrue(adminUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("CLIENT")));

        User defaultUser =new User();
        defaultUser.setEmail("user@example.com");
        defaultUser.setPassword("user123");
        defaultUser.setRole(Role.CLIENT);

        assertEquals(1, defaultUser.getAuthorities().size());
        assertTrue(defaultUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("CLIENT")));
    }

    @Test
    void testUserAccountStatus() {
        // Test isEnabled method
        User enabledUser= new User();
        enabledUser.setEmail("user@example.com");
        enabledUser.setPassword("user123");
        enabledUser.setRole(Role.CLIENT);

        assertTrue(enabledUser.isEnabled());
        assertTrue(enabledUser.isAccountNonExpired());
        assertTrue(enabledUser.isAccountNonLocked());
        assertTrue(enabledUser.isCredentialsNonExpired());
    }

}
