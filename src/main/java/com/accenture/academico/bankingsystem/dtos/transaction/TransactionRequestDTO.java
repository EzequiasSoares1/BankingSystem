package com.accenture.academico.bankingsystem.dtos.transaction;

import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(
        @NotNull
        AccountType accountType,
        @NotNull
        UUID receiverId,
        @NotNull
        BigDecimal value) {
}
