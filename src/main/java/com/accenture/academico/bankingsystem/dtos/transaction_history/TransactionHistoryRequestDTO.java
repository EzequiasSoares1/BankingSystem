package com.accenture.academico.bankingsystem.dtos.transaction_history;

import com.accenture.academico.bankingsystem.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionHistoryRequestDTO(
        UUID accountId,
        TransactionType transactionType,
        BigDecimal value) {
}
