package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.dtos.account.AccountResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.OperationRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.transaction_history.TransactionHistoryRequestDTO;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.mappers.account.AccountMapper;
import com.accenture.academico.bankingsystem.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

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
                        TransactionType.DEPOSIT,
                        transactionDTO.valueTransaction(),
                        transactionDTO.balance()
                )
        );
        return transactionDTO;
    }
    public void sac(OperationRequestDTO operationDTO){

//        transactionHistoryService.createTransactionHistory(
//                new TransactionHistoryRequestDTO(
//                        account.getId(),
//                        TransactionType.PAYMENT,
//                        operationDTO.value(),
//                        account.getBalance()
//                )
//        );
        //return AccountMapper.convertToAccountResponseDTO(account);
    }
}
