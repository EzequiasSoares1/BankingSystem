package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final TransactionRepository transactionRepository;
}
