package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.domain.transation_history.TransactionHistory;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryResponseDTO;
import com.accenture.academico.bankingsystem.mappers.transaction_history.TransactionHistoryMapper;
import com.accenture.academico.bankingsystem.repositories.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final AccountService accountService;
    public void createTransactionHistory(TransactionHistoryRequestDTO transactionHistoryDTO){
        TransactionHistory transactionHistory = new TransactionHistory();

        transactionHistory.setAccount(accountService.getById(transactionHistoryDTO.accountId()));
        transactionHistory.setTransactionType(transactionHistoryDTO.transactionType());
        transactionHistory.setAmount(transactionHistoryDTO.value());
        transactionHistory.setBalanceCurrent(transactionHistoryDTO.balanceCurrent());

        transactionHistoryRepository.save(transactionHistory);
    }

    public List<TransactionHistoryResponseDTO> getAllTransactionHistoryByAccountId(UUID accountId){
        return TransactionHistoryMapper.convertToTransactionHistoryResponseDTOList(transactionHistoryRepository.findByAccountId(accountId));
    }
}
