package com.accenture.academico.bankingsystem.dto.address;

import java.util.UUID;

public record AddressResponseDTO(
        UUID id,
        String cep,
        String number,
        String street,
        String district) {
}
