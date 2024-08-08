package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.domain.transation_history.TransactionHistory;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;
import com.accenture.academico.bankingsystem.repositories.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final AccountService accountService;
    public final void createTransactionHistory(TransactionHistoryRequestDTO transactionHistoryDTO){
        TransactionHistory transactionHistory = new TransactionHistory();

        transactionHistory.setAccount(accountService.getById(transactionHistoryDTO.accountId()));
        transactionHistory.setTransactionType(transactionHistoryDTO.transactionType());
        transactionHistory.setAmount(transactionHistoryDTO.value());

        transactionHistoryRepository.save(transactionHistory);
    }
}
