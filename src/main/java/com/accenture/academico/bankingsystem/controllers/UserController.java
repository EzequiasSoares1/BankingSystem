package com.accenture.academico.bankingsystem.controllers;
import com.accenture.academico.bankingsystem.dtos.user.UserDTO;
import com.accenture.academico.bankingsystem.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public  ResponseEntity<Object> create(@RequestBody @Valid UserDTO userDTO) {
        log.info("Creating user with DTO: {}", userDTO.toString());
        return ResponseEntity.created(null).body(userService.saveUser(userDTO));
    }

    @GetMapping
    public ResponseEntity<Object>getAll(){
        log.info("Fetching all users");
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        log.info("Fetching user with id: {}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/email/{email}")
    public  ResponseEntity<Object> getByEmail(@PathVariable @Email String email) {
        log.info("Fetching user with email: {}", email);
        return ResponseEntity.ok().body(userService.getUserByEmail(email));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid UserDTO userDTO){
        log.info("Updating user with id {} and DTO: {}", id, userDTO);
        return ResponseEntity.ok(userService.update(id,userDTO));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Object> delete(@PathVariable UUID id){
        log.info("Deleting user with id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
