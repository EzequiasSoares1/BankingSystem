package com.accenture.academico.bankingsystem.domain.address;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @Column(nullable = false, length = 255)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, length = 10)
    private String cep;
    @Column(nullable = false, length = 10)
    private String number;
    @Column(nullable = false, length = 100)
    private String street;
    @Column(nullable = false, length = 100)
    private String district;
}
