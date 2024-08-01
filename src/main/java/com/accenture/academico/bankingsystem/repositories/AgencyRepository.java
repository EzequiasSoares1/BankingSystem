package com.accenture.academico.bankingsystem.repositories;

import com.accenture.academico.bankingsystem.domain.agency.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency, String> {
}
