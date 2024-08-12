package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.domain.transation_history.TransactionHistory;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryResponseDTO;
import com.accenture.academico.bankingsystem.mappers.transaction_history.TransactionHistoryMapper;
import com.accenture.academico.bankingsystem.repositories.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final AccountService accountService;

    public void createTransactionHistory(TransactionHistoryRequestDTO transactionHistoryDTO){
        log.debug("Creating transaction history with data: {}", transactionHistoryDTO);

        TransactionHistory transactionHistory = new TransactionHistory();

        transactionHistory.setAccount(accountService.getById(transactionHistoryDTO.accountId()));
        transactionHistory.setTransactionType(transactionHistoryDTO.transactionType());
        transactionHistory.setAmount(transactionHistoryDTO.value());
        transactionHistory.setBalanceCurrent(transactionHistoryDTO.balanceCurrent());

        transactionHistoryRepository.save(transactionHistory);

        log.info("Transaction history created and saved for account ID: {}", transactionHistoryDTO.accountId());

    }

    public List<TransactionHistoryResponseDTO> getAllTransactionHistoryByAccountId(UUID accountId){
        log.debug("Retrieving transaction history for account ID: {}", accountId);

        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findByAccountId(accountId);
        if (!transactionHistoryList.isEmpty())
            transactionHistoryList.sort(Comparator.comparing(TransactionHistory::getTransactionDate).reversed());
        return TransactionHistoryMapper.convertToTransactionHistoryResponseDTOList(transactionHistoryList);
    }
}
