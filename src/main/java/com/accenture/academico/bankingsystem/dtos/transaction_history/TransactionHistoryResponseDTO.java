package com.accenture.academico.bankingsystem.dtos.transaction_history;

import com.accenture.academico.bankingsystem.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionHistoryResponseDTO(
        LocalDateTime transactionDate,
        TransactionType transactionType,
        BigDecimal amount,
        BigDecimal balanceCurrent
) {
}
