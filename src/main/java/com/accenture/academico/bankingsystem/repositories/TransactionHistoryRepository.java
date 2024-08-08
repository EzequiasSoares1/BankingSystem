package com.accenture.academico.bankingsystem.repositories;
import com.accenture.academico.bankingsystem.domain.transation_history.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, UUID> {
}
