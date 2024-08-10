package com.accenture.academico.bankingsystem.dtos.pix_key;

import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.PixKeyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PixKeyRequestDTO(
        @NotNull
        PixKeyType keyType,
        @NotBlank
        String keyValue

) {
}
