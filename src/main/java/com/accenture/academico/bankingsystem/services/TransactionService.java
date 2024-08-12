package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.dtos.pix_key.PixRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.OperationRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransferResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;
import com.accenture.academico.bankingsystem.email.service.NotificationEmailService;
import com.accenture.academico.bankingsystem.mappers.transaction.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final AccountService accountService;
    private final TransactionHistoryService transactionHistoryService;
    private final NotificationEmailService notificationEmailService;

    public TransactionResponseDTO deposit(OperationRequestDTO operationDTO){
        TransactionResponseDTO transactionDTO = accountService.deposit(operationDTO);
        log.info("Deposit completed: {}", transactionDTO);

        notificationEmailService.sendReceipt(transactionDTO);

        transactionHistoryService.createTransactionHistory(
                TransactionMapper.convertToTransactionHistoryRequestDTO(transactionDTO)
        );

        log.debug("Transaction history recorded for account ID: {}", transactionDTO.accountId());

        return transactionDTO;
    }
    public TransactionResponseDTO withdraw(OperationRequestDTO operationDTO){
        TransactionResponseDTO transactionDTO = accountService.withdraw(operationDTO);

        log.info("Withdrawal completed: {}", transactionDTO);

        notificationEmailService.sendReceipt(transactionDTO);

        transactionHistoryService.createTransactionHistory(
                TransactionMapper.convertToTransactionHistoryRequestDTO(transactionDTO)
        );

        log.debug("Transaction history recorded for account ID: {}", transactionDTO.accountId());

        return transactionDTO;
    }
    public TransactionResponseDTO transfer(TransactionRequestDTO transactionDTO){
        TransferResponseDTO transferDTO = accountService.transfer(transactionDTO);

        this.createTransactionHistoryByTransfer(transferDTO);

        log.info("Transfer completed: {}", transferDTO);

        notificationEmailService.sendReceiptTransfer(transferDTO);

        log.debug("Transaction history recorded for sender account ID: {}", transferDTO.senderId());

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

        log.info("PIX transfer completed: {}", pixDTO);

        notificationEmailService.sendReceiptTransfer(transferDTO);

        log.debug("Transaction history recorded for sender account ID: {}", transferDTO.senderId());

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