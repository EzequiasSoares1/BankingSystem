package com.accenture.academico.bankingsystem.dtos.client;

import com.accenture.academico.bankingsystem.domain.address.Address;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ClientResponseDTO(
        UUID id,
        String name,
        String cpf,
        String telephone,
        UUID address_id,
        UUID user_id
        ) {
}
