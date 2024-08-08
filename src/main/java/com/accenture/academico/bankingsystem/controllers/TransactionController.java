package com.accenture.academico.bankingsystem.controllers;

import com.accenture.academico.bankingsystem.dtos.transaction.OperationRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@RequestBody @Valid OperationRequestDTO body){
        return ResponseEntity.ok(transactionService.deposit(body));
    }

    @PostMapping("/sac")
    public ResponseEntity<TransactionResponseDTO> sac(@RequestBody @Valid OperationRequestDTO body){
        return ResponseEntity.ok(transactionService.sac(body));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@RequestBody @Valid TransactionRequestDTO body){
        return ResponseEntity.ok(transactionService.transfer(body));
    }
}
