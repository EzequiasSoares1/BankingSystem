package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.dtos.transaction.OperationRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionTransferResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountService accountService;
    private final TransactionHistoryService transactionHistoryService;
    public TransactionResponseDTO deposit(OperationRequestDTO operationDTO){
        TransactionResponseDTO transactionDTO = accountService.deposit(operationDTO.accountId(), operationDTO.value());

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
    public TransactionResponseDTO sac(OperationRequestDTO operationDTO){
        TransactionResponseDTO transactionDTO = accountService.sac(operationDTO.accountId(), operationDTO.value());

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
    public TransactionResponseDTO transfer(TransactionRequestDTO request){
        TransactionTransferResponseDTO transactionTransferResponseDTO = accountService.transfer(request.senderId(), request.receiverId(), request.value());

        transactionHistoryService.createTransactionHistory(
                new TransactionHistoryRequestDTO(
                        transactionTransferResponseDTO.senderId(),
                        transactionTransferResponseDTO.transactionType(),
                        transactionTransferResponseDTO.valueTransaction().negate(),
                        transactionTransferResponseDTO.senderBalance()
                )
        );
        transactionHistoryService.createTransactionHistory(
                new TransactionHistoryRequestDTO(
                        transactionTransferResponseDTO.receiverId(),
                        transactionTransferResponseDTO.transactionType(),
                        transactionTransferResponseDTO.valueTransaction(),
                        transactionTransferResponseDTO.receiverBalance()
                )
        );
        return new TransactionResponseDTO(
                transactionTransferResponseDTO.accountType(),
                transactionTransferResponseDTO.transactionType(),
                transactionTransferResponseDTO.agencyId(),
                transactionTransferResponseDTO.senderId(),
                transactionTransferResponseDTO.senderBalance(),
                transactionTransferResponseDTO.dataTransaction(),
                transactionTransferResponseDTO.valueTransaction()
        );
    }
}