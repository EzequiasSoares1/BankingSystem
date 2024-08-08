package com.accenture.academico.bankingsystem.dtos.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record OperationRequestDTO(
        @NotNull
        UUID accountId,
        @NotNull
        BigDecimal value) {
}
