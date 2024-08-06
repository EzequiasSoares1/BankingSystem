package com.accenture.academico.bankingsystem.domain.address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 10)
    @NotBlank
    private String cep;

    @Column(nullable = false, length = 10)
    @NotBlank
    private String number;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String street;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String district;
}
