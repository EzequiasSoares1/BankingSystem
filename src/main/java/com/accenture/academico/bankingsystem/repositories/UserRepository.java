package com.accenture.academico.bankingsystem.repositories;

import com.accenture.academico.bankingsystem.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findById(UUID id);
    UserDetails findByEmail(String email);
    Optional<User> findUserByEmail(String email);
}
