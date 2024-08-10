package com.accenture.academico.bankingsystem.dtos.pix_key;

import com.accenture.academico.bankingsystem.domain.enums.AccountType;

import java.math.BigDecimal;

public record PixRequestDTO(
        String pixKey,
        AccountType accountType,
        BigDecimal value
) {
}
