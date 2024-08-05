package com.accenture.academico.bankingsystem.unit.converter;


import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dto.UserDTO;
import com.accenture.academico.bankingsystem.services.converter.UserConverter;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserConverterTest {

    @Test
    void testConvertToUser() {
        UserDTO userDTO = new UserDTO(UUID.randomUUID(), "test@example.com", "password", "ADMIN");
        User user = UserConverter.convertToUser(userDTO);

        assertNotNull(user);
        assertEquals(userDTO.id(), user.getId());
        assertEquals(userDTO.email(), user.getEmail());
        assertEquals(userDTO.role(), user.getRole().toString());
        assertEquals(userDTO.password(), user.getPassword());
    }

    @Test
    void testConvertToUserDTO() {
        User user = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        UserDTO userDTO = UserConverter.convertToUserDTO(user);

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.id());
        assertEquals(user.getEmail(), userDTO.email());
        assertEquals(user.getRole().toString(), userDTO.role());
    }
}
