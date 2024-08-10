package com.accenture.academico.bankingsystem.dtos.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record UserDTO(
      UUID id,

      @NotBlank(message = "Email cannot be blank")
      @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
      String email,

      @NotBlank(message = "Password cannot be blank")
      String password,

      @NotBlank(message = "Role cannot be blank")
      String role
) {
}
