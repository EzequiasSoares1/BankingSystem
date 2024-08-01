package com.accenture.academico.bankingsystem.domain.client;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.address.Address;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "client")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, length = 45)
    private String name;
    @Column(nullable = false, length = 20)
    private String cpf;
    @Column(nullable = false, length = 100)
    private String telephone;
    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @OneToMany
    @JoinColumn(name = "account_id", nullable = false)
    private List<Account> account;
}