package com.accenture.academico.bankingsystem.dtos.agency;

import com.accenture.academico.bankingsystem.domain.address.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AgencyRequestDTO(
        @JsonIgnore UUID id,
        @NotBlank
        String name,
        @NotBlank
        String telephone,
        @NotBlank
        String number,
        @NotNull
        UUID addressId
){}
