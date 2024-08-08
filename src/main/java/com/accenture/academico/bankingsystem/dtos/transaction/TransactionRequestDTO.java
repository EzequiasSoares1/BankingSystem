package com.accenture.academico.bankingsystem.dtos.transaction;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(
        @NotNull
        UUID senderId,
        @NotNull
        UUID receiverId,
        @NotNull
        BigDecimal value) {
}
