package com.accenture.academico.bankingsystem.controllers;

import com.accenture.academico.bankingsystem.dtos.pix_key.PixRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.OperationRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryResponseDTO;
import com.accenture.academico.bankingsystem.services.TransactionHistoryService;
import com.accenture.academico.bankingsystem.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionHistoryService transactionHistoryService;

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

    @PostMapping("/pix")
    public ResponseEntity<TransactionResponseDTO> transfer(@RequestBody @Valid PixRequestDTO body){
        return ResponseEntity.ok(transactionService.pix(body));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TransactionHistoryResponseDTO>> getAllTransactionHistoryByAccountId(@PathVariable UUID id){
        return ResponseEntity.ok(transactionHistoryService.getAllTransactionHistoryByAccountId(id));
    }
}
