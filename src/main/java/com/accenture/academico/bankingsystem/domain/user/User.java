package com.accenture.academico.bankingsystem.domain.user;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Email(message = "Invalid email")
    private String email;

    public User(UUID id, String email, Role role, String password) {
        this.email = email;
        this.id = id;
        this.role = role;
        this.password = password;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
    private String password;
    private String client_id;

}
