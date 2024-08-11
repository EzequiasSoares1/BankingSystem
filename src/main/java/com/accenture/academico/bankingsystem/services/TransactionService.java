package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.dtos.pix_key.PixRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.OperationRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransferResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountService accountService;
    private final TransactionHistoryService transactionHistoryService;
    public TransactionResponseDTO deposit(OperationRequestDTO operationDTO){
        TransactionResponseDTO transactionDTO = accountService.deposit(operationDTO);

        transactionHistoryService.createTransactionHistory(
                new TransactionHistoryRequestDTO(
                        transactionDTO.accountId(),
                        transactionDTO.transactionType(),
                        transactionDTO.valueTransaction(),
                        transactionDTO.balance()
                )
        );
        return transactionDTO;
    }
    public TransactionResponseDTO withdraw(OperationRequestDTO operationDTO){
        TransactionResponseDTO transactionDTO = accountService.withdraw(operationDTO);

        transactionHistoryService.createTransactionHistory(
                new TransactionHistoryRequestDTO(
                        transactionDTO.accountId(),
                        transactionDTO.transactionType(),
                        transactionDTO.valueTransaction(),
                        transactionDTO.balance()
               )
        );
        return transactionDTO;
    }
    public TransactionResponseDTO transfer(TransactionRequestDTO transactionDTO){
        TransferResponseDTO transferDTO = accountService.transfer(transactionDTO);
        this.createTransactionHistoryByTransfer(transferDTO);

        return new TransactionResponseDTO(
                transferDTO.accountType(),
                transferDTO.transactionType(),
                transferDTO.agencyId(),
                transferDTO.senderId(),
                transferDTO.senderBalance(),
                transferDTO.dataTransaction(),
                transferDTO.valueTransaction()
        );
    }

    public TransactionResponseDTO pix(PixRequestDTO pixDTO){
        TransferResponseDTO transferDTO = this.accountService.pix(pixDTO);
        this.createTransactionHistoryByTransfer(transferDTO);

        return new TransactionResponseDTO(
                transferDTO.accountType(),
                transferDTO.transactionType(),
                transferDTO.agencyId(),
                transferDTO.senderId(),
                transferDTO.senderBalance(),
                transferDTO.dataTransaction(),
                transferDTO.valueTransaction()
        );
    }
    private void createTransactionHistoryByTransfer(TransferResponseDTO transferDTO){
        transactionHistoryService.createTransactionHistory(
                new TransactionHistoryRequestDTO(
                        transferDTO.senderId(),
                        transferDTO.transactionType(),
                        transferDTO.valueTransaction().negate(),
                        transferDTO.senderBalance()
                )
        );
        transactionHistoryService.createTransactionHistory(
                new TransactionHistoryRequestDTO(
                        transferDTO.receiverId(),
                        transferDTO.transactionType(),
                        transferDTO.valueTransaction(),
                        transferDTO.receiverBalance()
                )
        );
    }
}