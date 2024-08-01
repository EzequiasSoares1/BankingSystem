package com.accenture.academico.bankingsystem.domain.user;

import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotBlank
    @Email(message = "Invalid email")
    @Column(nullable = false, length = 100)
    private String email;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false, length = 100)
    private String password;
    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}
