package com.accenture.academico.bankingsystem.mappers.transaction_history;

import com.accenture.academico.bankingsystem.domain.transation_history.TransactionHistory;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionHistoryMapper {
    public static List<TransactionHistoryResponseDTO> convertToTransactionHistoryResponseDTOList(List<TransactionHistory> transactionHistoryList){
        return transactionHistoryList.stream()
                .map(transactionHistory -> new TransactionHistoryResponseDTO(
                        transactionHistory.getTransactionDate(),
                        transactionHistory.getTransactionType(),
                        transactionHistory.getAmount(),
                        transactionHistory.getBalanceCurrent()
                ))
                .collect(Collectors.toList());
    }
}
