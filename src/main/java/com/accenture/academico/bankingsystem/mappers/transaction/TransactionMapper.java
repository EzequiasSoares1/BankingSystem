package com.accenture.academico.bankingsystem.mappers.transaction;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionTransferResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;

import java.math.BigDecimal;

public class TransactionMapper {
    public static TransactionResponseDTO convertToTransactionResponseDTO(Account account, TransactionType transactionType, BigDecimal valueTransaction) {

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

    public static TransactionTransferResponseDTO convertToTransactionTransferResponseDTO(Account fromAccount, Account toAccount, BigDecimal amount, TransactionType transactionType) {
        return new TransactionTransferResponseDTO(
                fromAccount.getId(),
                toAccount.getId(),
                fromAccount.getBalance(),
                toAccount.getBalance(),
                fromAccount.getAccountType(),
                transactionType,
                fromAccount.getAgency().getId(),
                fromAccount.getUpdatedDate(),
                amount
        );
    }

    public static TransactionHistoryRequestDTO convertToTransactionHistoryRequestDTO(TransactionResponseDTO transactionResponseDTO) {
        return new TransactionHistoryRequestDTO(
                transactionResponseDTO.accountId(),
                transactionResponseDTO.transactionType(),
                transactionResponseDTO.valueTransaction(),
                transactionResponseDTO.balance()
        );
    }

    public static TransactionHistoryRequestDTO convertToTransactionHistoryRequestDTOFromSender(TransactionTransferResponseDTO transferResponseDTO) {
        return new TransactionHistoryRequestDTO(
                transferResponseDTO.senderId(),
                transferResponseDTO.transactionType(),
                transferResponseDTO.valueTransaction().negate(),
                transferResponseDTO.senderBalance()
        );
    }

    public static TransactionResponseDTO convertToTransactionResponseDTO(TransactionTransferResponseDTO transferResponseDTO) {
        return new TransactionResponseDTO(
                transferResponseDTO.accountType(),
                transferResponseDTO.transactionType(),
                transferResponseDTO.agencyId(),
                transferResponseDTO.receiverId(),
                transferResponseDTO.receiverBalance(),
                transferResponseDTO.dataTransaction(),
                transferResponseDTO.valueTransaction()
        );
    }
}
