package com.accenture.academico.bankingsystem.controllers;
import com.accenture.academico.bankingsystem.dtos.account.AccountRequestDTO;
import com.accenture.academico.bankingsystem.dtos.account.AccountResponseDTO;
import com.accenture.academico.bankingsystem.dtos.account.AccountUpdateDTO;
import com.accenture.academico.bankingsystem.services.AccountService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        log.info("Fetching all accounts");
        return ResponseEntity.ok().body(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable UUID id) {
        log.info("Fetching account with id: {}", id);
        return ResponseEntity.ok().body(accountService.getAccountById(id));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        log.info("Creating account with DTO: {}", accountRequestDTO.toString());
        return ResponseEntity.ok().body(accountService.createAccount(accountRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable UUID id, @RequestBody @Valid AccountUpdateDTO accountUpdateDTO) {
        log.info("Updating account with id {} and DTO: {}", id, accountUpdateDTO);
        return ResponseEntity.ok().body(accountService.updateAccount(id, accountUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        log.info("Deleting account with id: {}", id);
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
