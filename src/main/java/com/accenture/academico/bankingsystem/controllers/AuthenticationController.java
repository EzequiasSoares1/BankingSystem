package com.accenture.academico.bankingsystem.controllers;
import com.accenture.academico.bankingsystem.dtos.user.AuthenticationDTO;
import com.accenture.academico.bankingsystem.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        log.info("User authenticate in login: {}", authenticationDTO.email());
        return ResponseEntity.ok(authenticationService.login(authenticationDTO));
    }

    @PostMapping("/isValidToken")
    public ResponseEntity<Object> isValidToken(@RequestParam String token){
        log.info("Validation to token: {}", token);
        authenticationService.validateToken(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Object> tokenRefresh(@RequestParam String token){
        log.info("Generater to tokenRefresh: {}", token);
        return ResponseEntity.ok(authenticationService.tokenRefresh(token));
    }
}
