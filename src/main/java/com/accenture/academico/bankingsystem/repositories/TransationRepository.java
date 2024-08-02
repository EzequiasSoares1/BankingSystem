package com.accenture.academico.bankingsystem.repositories;
import com.accenture.academico.bankingsystem.domain.transation.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransationRepository extends JpaRepository<TransactionHistory, UUID> {
}
