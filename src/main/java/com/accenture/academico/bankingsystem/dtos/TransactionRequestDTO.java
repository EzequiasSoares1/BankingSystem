package com.accenture.academico.bankingsystem.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(UUID senderId, UUID receiverId, BigDecimal value) {
}
