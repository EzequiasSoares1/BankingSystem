package com.accenture.academico.bankingsystem.dtos.account;

import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountTransactionResponseDTO (
        AccountType accountType,
        TransactionType transactionType,
        UUID agencyId,
        UUID clientId,
        BigDecimal balance,
        LocalDateTime dataTransaction,
        BigDecimal valeuTransaction
){
}
