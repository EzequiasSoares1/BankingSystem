package com.accenture.academico.bankingsystem.repositories;

import com.accenture.academico.bankingsystem.domain.pix_key.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PixKeyRepository extends JpaRepository<PixKey, UUID> {
    List<PixKey> findByAccountId(UUID accountId);
    Optional<PixKey> findByKeyValue(String keyValue);
}
