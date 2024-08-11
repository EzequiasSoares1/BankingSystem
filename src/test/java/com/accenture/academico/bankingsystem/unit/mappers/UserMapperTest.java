package com.accenture.academico.bankingsystem.unit.mappers;


import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dtos.user.UserDTO;
import com.accenture.academico.bankingsystem.mappers.user.UserMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    @Test
    void testConvertToUser() {
        UserDTO userDTO = new UserDTO(UUID.randomUUID(), "test@example.com", "password", "ADMIN");
        User user = UserMapper.convertToUser(userDTO);

        assertNotNull(user);
        assertEquals(userDTO.id(), user.getId());
        assertEquals(userDTO.email(), user.getEmail());
        assertEquals(userDTO.role(), user.getRole().toString());
        assertEquals(userDTO.password(), user.getPassword());
    }

    @Test
    void testConvertToUserDTO() {
        User user = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        UserDTO userDTO = UserMapper.convertToUserDTO(user);

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.id());
        assertEquals(user.getEmail(), userDTO.email());
        assertEquals(user.getRole().toString(), userDTO.role());
    }

    @Test
    void testConvertToUserDTOList(){
        User user1 = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");
        User user2 = new User(UUID.randomUUID(), "test@example.com", Role.ADMIN, "password");

        List<User> userList = List.of(user1, user2);
        List<UserDTO> userDTOList = UserMapper.convertToUserDTOList(userList);

        assertNotEquals(userDTOList.size(), 0);
        assertEquals(userDTOList.get(0).id(), userList.get(0).getId());
        assertEquals(userDTOList.get(1).id(), userList.get(1).getId());
        assertEquals(userDTOList.get(0).email(), userList.get(0).getEmail());
        assertEquals(userDTOList.get(1).email(), userList.get(1).getEmail());
        assertEquals(userDTOList.get(0).role(), userList.get(0).getRole().toString());
        assertEquals(userDTOList.get(1).role(), userList.get(1).getRole().toString());
    }
}
