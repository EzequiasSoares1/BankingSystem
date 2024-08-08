package com.accenture.academico.bankingsystem.dtos.account;

import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AccountRequestDTO (
                  @NotNull
                  AccountType accountType,
                  @NotNull
                  UUID agencyId,
                  @NotNull
                  UUID clientId
){
}
