package com.accenture.academico.bankingsystem.dto.address;

import jakarta.validation.constraints.NotBlank;

public record AddressRequestDTO(
        @NotBlank()
        String cep,
        @NotBlank()
        String number,
        @NotBlank()
        String street,
        @NotBlank()
        String district) {
}
