package com.accenture.academico.bankingsystem.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequestDTO(@NotBlank @Email String email, @NotBlank String password){

}
