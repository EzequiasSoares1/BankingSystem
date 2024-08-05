package com.accenture.academico.bankingsystem.unit.middlewares;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.middlewares.UserTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserToolsTest {

    private SecurityContext securityContext;
    private Authentication authentication;
    private User user;

    @BeforeEach
    void setUp() {
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("password123")
                .build();
        when(authentication.getPrincipal()).thenReturn(user);
        when(authentication.isAuthenticated()).thenReturn(true);
    }

    @Test
    void testIsAutorizate_Success() {
        UUID userId = user.getId();
        UserTools.isAutorizate(userId);
    }

    @Test
    void testIsAutorizate_NotAuthorized() {
        UUID differentUserId = UUID.randomUUID();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            UserTools.isAutorizate(differentUserId);
        });

        assertEquals("Nao autorizado", exception.getMessage());
    }

    @Test
    void testGetUserContextId() {
        UUID userId = user.getId();
        UUID returnedId = UserTools.getUserContextId();
        assertEquals(userId, returnedId);
    }

    @Test
    void testGetUserContextId_NoAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            UserTools.getUserContextId();
        });
    }
}
