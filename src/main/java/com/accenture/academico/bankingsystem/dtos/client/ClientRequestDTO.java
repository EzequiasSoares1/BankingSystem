package com.accenture.academico.bankingsystem.dtos.client;

import com.accenture.academico.bankingsystem.domain.address.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

public record ClientRequestDTO(
        @NotBlank
        String name,
        @CPF
        String cpf,
        @NotBlank
        String telephone,
        UUID address_id
) {
}
