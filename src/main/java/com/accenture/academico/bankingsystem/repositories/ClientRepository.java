package com.accenture.academico.bankingsystem.repositories;

import com.accenture.academico.bankingsystem.domain.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
}
