package com.accenture.academico.bankingsystem.repositories;

import com.accenture.academico.bankingsystem.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
