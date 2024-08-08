package com.accenture.academico.bankingsystem.dtos.transaction;

import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDTO(
        AccountType accountType,
        TransactionType transactionType,
        UUID agencyId,
        UUID accountId,
        BigDecimal balance,
        LocalDateTime dataTransaction,
        BigDecimal valueTransaction
){
}
