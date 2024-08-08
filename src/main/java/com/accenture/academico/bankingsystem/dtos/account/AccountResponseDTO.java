package com.accenture.academico.bankingsystem.dtos.account;

import com.accenture.academico.bankingsystem.domain.enums.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponseDTO(
                                UUID id,
                                String number,
                                AccountType accountType,
                                UUID agencyId,
                                BigDecimal balance,
                                UUID clientId
) {}
