package com.accenture.academico.bankingsystem.domain.agency;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "agency")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 45)
    @NotBlank
    private String name;

    @Column(nullable = false, length = 20)
    @NotBlank
    private String telephone;

    @Column(nullable = false, length = 10)
    @NotBlank
    private String number;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    @NotNull
    private Address address;
    @Column(nullable = false)
    private LocalDateTime  createdDate;
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

    public Agency(UUID id, String name, String telephone, String number, Address address) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.number = number;
        this.address = address;
    }
}