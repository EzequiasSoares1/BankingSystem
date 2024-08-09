package com.accenture.academico.bankingsystem.dtos.transaction;

import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionTransferResponseDTO(
        UUID senderId,
        UUID receiverId,
        BigDecimal senderBalance,
        BigDecimal receiverBalance,
        AccountType accountType,
        TransactionType transactionType,
        UUID agencyId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime dataTransaction,
        BigDecimal valueTransaction
) {
}
