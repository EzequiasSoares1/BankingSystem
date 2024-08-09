package com.accenture.academico.bankingsystem.dtos.transaction_history;

import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionHistoryResponseDTO(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime transactionDate,
        TransactionType transactionType,
        BigDecimal amount,
        BigDecimal balanceCurrent
) {
}
