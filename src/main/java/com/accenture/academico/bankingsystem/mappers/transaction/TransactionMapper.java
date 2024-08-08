package com.accenture.academico.bankingsystem.mappers.transaction;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;

import java.math.BigDecimal;

public class TransactionMapper {
    public static TransactionResponseDTO convertToAccountTransactionResponseDTO(Account account, TransactionType transactionType, BigDecimal valueTransaction) {

        return new TransactionResponseDTO(
                account.getAccountType(),
                transactionType,
                account.getAgency().getId(),
                account.getId(),
                account.getBalance(),
                account.getUpdatedDate(),
                valueTransaction
        );
    }
}
