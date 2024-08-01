package com.accenture.academico.bankingsystem.repositories;

import com.accenture.academico.bankingsystem.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
