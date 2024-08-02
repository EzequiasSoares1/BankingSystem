package com.accenture.academico.bankingsystem.domain.address;

import com.accenture.academico.bankingsystem.dto.address.AddressRequestDTO;
import jakarta.persistence.*;
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
    private String cep;

    @Column(nullable = false, length = 10)
    private String number;

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 100)
    private String district;

    public Address(AddressRequestDTO addressDTO){
        this.cep = addressDTO.cep();
        this.number = addressDTO.number();
        this.street = addressDTO.street();
        this.district = addressDTO.district();
    }
}
