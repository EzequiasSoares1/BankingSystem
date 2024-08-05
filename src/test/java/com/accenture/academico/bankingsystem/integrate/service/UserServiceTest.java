package com.accenture.academico.bankingsystem.integrate.service;

import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dto.UserDTO;
import com.accenture.academico.bankingsystem.repositories.UserRepository;
import com.accenture.academico.bankingsystem.services.general.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest implements ConfigSpringTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void testLoadUserByUsername() {
        String email = "test@example.com";
        User user = new User(null, "test@example.com", Role.ADMIN, "ADMIN");
        user.setEmail(email);
        userRepository.save(user);

        User loadedUser = (User) userService.loadUserByUsername(email);

        assertNotNull(loadedUser);
        assertEquals(email, loadedUser.getEmail());
    }

    @Test
    void testGetUserByEmail() {
        String email = "test@example.com";
        User user = new User(null, "test@example.com", Role.ADMIN, "ADMIN");

        user.setEmail(email);
        userRepository.save(user);

        UserDTO userDTO = userService.getUserByEmail(email);

        assertNotNull(userDTO);
        assertEquals(email, userDTO.email());
    }

    @Test
    void testGetUserById() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "test@example.com", Role.ADMIN, "ADMIN");
        user = userRepository.save(user);

        UserDTO userDTO = userService.getUserById(user.getId());

        assertNotNull(userDTO);
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = Collections.singletonList(new User(null, "test@example.com", Role.ADMIN, "ADMIN"));
        userRepository.saveAll(userList);

        List<UserDTO> userDTOList = userService.getAllUsers();

        assertNotNull(userDTOList);
        assertEquals(userList.size(), userDTOList.size());
    }

    @Test
    void testSaveUser() {
        UserDTO userDTO = new UserDTO(null, "testss@example.com", "password", "ADMIN");

        userService.saveUser(userDTO);

        UserDTO savedUser = userService.getUserByEmail(userDTO.email());
        assertEquals(userDTO.email(), savedUser.email());
    }

    @Test
    void testDeleteUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "test@example.com", Role.ADMIN, "ADMIN");

        user = userRepository.save(user);

        userService.deleteUser(user.getId());
        assertTrue(userRepository.findById(id).isEmpty());
    }

    @Test
    void testUpdate() {
        User user = new User(null, "test@example.com", Role.ADMIN, "ADMIN");
        user.setEmail("test@example.com");
        userRepository.save(user);

        UserDTO userDTO = new UserDTO(user.getId(), "updated@example.com", null, Role.ADMIN.name());

        userService.update(user.getId(),userDTO);

        User updatedUser = (User) userRepository.findByEmail(userDTO.email());
        assertNotNull(updatedUser);
        assertEquals(userDTO.email(), updatedUser.getEmail());
        assertEquals(userDTO.role(), updatedUser.getRole().name());
    }
    @Test
    void testSaveUser_UserAlreadyExists() {
        UserDTO userDTO = new UserDTO(null, "test@example.com", "password", "ADMIN");
        User user = new User(null, "test@example.com", Role.ADMIN, "ADMIN");
        userRepository.save(user);

        assertThrows(RuntimeException.class, () -> userService.saveUser(userDTO));
    }

    @Test
    void testUpdate_UserNotFound() {
        UUID nonExistentUserId = UUID.randomUUID();
        UserDTO userDTO = new UserDTO(nonExistentUserId, "updated@example.com", null, "ADMIN");

        assertThrows(RuntimeException.class, () -> userService.update(nonExistentUserId,userDTO));
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        String nonExistentEmail = "nonexistent@example.com";

        assertThrows(RuntimeException.class, () -> userService.getUserByEmail(nonExistentEmail));
    }

    @Test
    void testGetUserById_UserNotFound() {
        UUID nonExistentUserId = UUID.randomUUID();

        assertThrows(RuntimeException.class, () -> userService.getUserById(nonExistentUserId));
    }

    @Test
    void testDeleteUser_UserNotFound() {
        UUID UserId = UUID.randomUUID();

        assertThrows(RuntimeException.class, () -> userService.deleteUser(UserId));
    }
}
