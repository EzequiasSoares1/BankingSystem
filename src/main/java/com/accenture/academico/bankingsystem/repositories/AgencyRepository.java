package com.accenture.academico.bankingsystem.repositories;

import com.accenture.academico.bankingsystem.domain.agency.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AgencyRepository extends JpaRepository<Agency, UUID> {
}
