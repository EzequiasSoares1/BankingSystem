package com.accenture.academico.bankingsystem.mappers.transaction;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.dtos.pix_key.PixRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransferResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;
import org.springframework.security.core.parameters.P;

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


    public static TransactionHistoryRequestDTO convertToTransactionHistoryRequestDTO(TransactionResponseDTO transactionResponseDTO) {
        return new TransactionHistoryRequestDTO(
                transactionResponseDTO.accountId(),
                transactionResponseDTO.transactionType(),
                transactionResponseDTO.valueTransaction(),
                transactionResponseDTO.balance()
        );
    }

    public static TransferResponseDTO convertToTransferResponseDTO(
            Account fromAccount,
            Account toAccount,
            TransactionRequestDTO transactionDTO,
            TransactionType transactionType
            ) {

        return new TransferResponseDTO(
                fromAccount.getId(),
                toAccount.getId(),
                fromAccount.getBalance(),
                toAccount.getBalance(),
                fromAccount.getAccountType(),
                transactionType,
                fromAccount.getAgency().getId(),
                fromAccount.getUpdatedDate(),
                transactionDTO.value()
        );
    }
    public static TransferResponseDTO convertToPIXResponseDTO(
            Account fromAccount,
            Account toAccount,
            PixRequestDTO pixRequestDTO,
            TransactionType transactionType
    ) {

        return new TransferResponseDTO(
                fromAccount.getId(),
                toAccount.getId(),
                fromAccount.getBalance(),
                toAccount.getBalance(),
                fromAccount.getAccountType(),
                transactionType,
                fromAccount.getAgency().getId(),
                fromAccount.getUpdatedDate(),
                pixRequestDTO.value()
        );
    }

}
