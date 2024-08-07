package com.accenture.academico.bankingsystem.domain.address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
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

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime updatedDate;

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        final LocalDateTime date = LocalDateTime.now();
        createdDate = date;
        updatedDate = date;
    }

    public Address(UUID id, String cep, String number, String street, String district) {
        this.id = id;
        this.cep = cep;
        this.number = number;
        this.street = street;
        this.district = district;
    }
}
