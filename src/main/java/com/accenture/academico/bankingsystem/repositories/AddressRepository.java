package com.accenture.academico.bankingsystem.repositories;

import com.accenture.academico.bankingsystem.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
