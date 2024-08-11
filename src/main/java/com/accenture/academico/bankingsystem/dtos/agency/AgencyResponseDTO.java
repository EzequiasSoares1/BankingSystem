package com.accenture.academico.bankingsystem.dtos.agency;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AgencyResponseDTO(
        UUID id,
        @NotBlank
        String name,
        @NotBlank
        String telephone,
        @NotBlank
        String number,
        UUID address_id
){}
