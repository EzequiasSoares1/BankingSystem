package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.dtos.pix_key.PixRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.OperationRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionTransferResponseDTO;
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
        TransactionResponseDTO transactionDTO = accountService.deposit(operationDTO.accountId(), operationDTO.value());
        log.info("Deposit completed: {}", transactionDTO);

        notificationEmailService.sendReceipt(transactionDTO);

        transactionHistoryService.createTransactionHistory(
                TransactionMapper.convertToTransactionHistoryRequestDTO(transactionDTO)
        );

        log.debug("Transaction history recorded for account ID: {}", transactionDTO.accountId());

        return transactionDTO;
    }

    public TransactionResponseDTO sac(OperationRequestDTO operationDTO){
        TransactionResponseDTO transactionDTO = accountService.sac(operationDTO.accountId(), operationDTO.value());

        log.info("Withdrawal completed: {}", transactionDTO);

        notificationEmailService.sendReceipt(transactionDTO);

        transactionHistoryService.createTransactionHistory(
                TransactionMapper.convertToTransactionHistoryRequestDTO(transactionDTO)
        );

        log.debug("Transaction history recorded for account ID: {}", transactionDTO.accountId());

        return transactionDTO;
    }

    public TransactionResponseDTO transfer(TransactionRequestDTO request){
        TransactionTransferResponseDTO transactionTransferResponseDTO = accountService.transfer(request.senderId(), request.receiverId(), request.value());

        log.info("Transfer completed: {}", transactionTransferResponseDTO);

        notificationEmailService.sendReceiptTransfer(transactionTransferResponseDTO);

        transactionHistoryService.createTransactionHistory(
                TransactionMapper.convertToTransactionHistoryRequestDTOFromSender(transactionTransferResponseDTO)
        );

        log.debug("Transaction history recorded for sender account ID: {}", transactionTransferResponseDTO.senderId());

        return TransactionMapper.convertToTransactionResponseDTO(transactionTransferResponseDTO);
    }

    public TransactionResponseDTO pix(PixRequestDTO pixDTO){
        TransactionTransferResponseDTO transactionTransferResponseDTO = this.accountService.pix(pixDTO);

        log.info("PIX transfer completed: {}", transactionTransferResponseDTO);

        notificationEmailService.sendReceiptTransfer(transactionTransferResponseDTO);

        transactionHistoryService.createTransactionHistory(
                TransactionMapper.convertToTransactionHistoryRequestDTOFromSender(transactionTransferResponseDTO)
        );

        log.debug("Transaction history recorded for sender account ID: {}", transactionTransferResponseDTO.senderId());

        return TransactionMapper.convertToTransactionResponseDTO(transactionTransferResponseDTO);
    }
}