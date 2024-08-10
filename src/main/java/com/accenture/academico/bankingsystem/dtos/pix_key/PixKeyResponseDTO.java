package com.accenture.academico.bankingsystem.dtos.pix_key;

import com.accenture.academico.bankingsystem.domain.enums.PixKeyType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record PixKeyResponseDTO(
        UUID id,
        PixKeyType keyType,
        String keyValue,
        UUID accountId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime createdDate
) {
}
