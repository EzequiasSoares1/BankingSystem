package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.domain.transation_history.TransactionHistory;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;
import com.accenture.academico.bankingsystem.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final TransactionRepository transactionRepository;
    public final void createTransactionHistory(TransactionHistoryRequestDTO transactionHistoryDTO){
        TransactionHistory transactionHistory = new TransactionHistory();

        //transactionHistory.setAccount();
        transactionHistory.setTransactionType(transactionHistoryDTO.transactionType());
        transactionHistory.setAmount(transactionHistoryDTO.value());
    }
}
