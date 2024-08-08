package com.accenture.academico.bankingsystem.dtos.user;

import java.math.BigDecimal;
import java.util.UUID;

public record OperationRequestDTO(UUID accountId, BigDecimal value) {
}
