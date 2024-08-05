package com.accenture.academico.bankingsystem.middlewares;

import com.accenture.academico.bankingsystem.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserTools {
    public static void isAutorizate(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            UUID userId = user.getId();

            if(id != userId) {throw new RuntimeException("Nao autorizado");}
        }
    }

    public static UUID getUserContextId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
