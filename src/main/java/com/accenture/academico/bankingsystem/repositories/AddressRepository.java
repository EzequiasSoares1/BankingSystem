package com.accenture.academico.bankingsystem.repositories;

import com.accenture.academico.bankingsystem.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
