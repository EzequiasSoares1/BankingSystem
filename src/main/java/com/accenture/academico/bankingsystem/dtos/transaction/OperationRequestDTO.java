package com.accenture.academico.bankingsystem.dtos.transaction;

import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record OperationRequestDTO(
        @NotNull
        AccountType accountType,
        @NotNull
        BigDecimal value) {
}
