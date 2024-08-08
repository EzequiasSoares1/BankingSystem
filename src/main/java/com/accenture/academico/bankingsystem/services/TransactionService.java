package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.dtos.account.AccountResponseDTO;
import com.accenture.academico.bankingsystem.dtos.user.OperationRequestDTO;
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

    private final AccountRepository accountRepository;

    public AccountResponseDTO createDeposit(OperationRequestDTO operationDTO){
        this.validateAmount(operationDTO.value());

        Account account = this.findById(operationDTO.accountId());
        account.setBalance(account.getBalance().add(operationDTO.value()));
        accountRepository.save(account);

        return AccountMapper.convertToAccountResponseDTO(account);
    }

    public AccountResponseDTO createWithdraw(OperationRequestDTO operationDTO){
        this.validateAmount(operationDTO.value());

        Account account = this.findById(operationDTO.accountId());

        if (account.getBalance().compareTo(operationDTO.value()) < 0)
            throw new IllegalArgumentException("Insufficient funds");

        account.setBalance(account.getBalance().subtract(operationDTO.value()));
        accountRepository.save(account);

        return AccountMapper.convertToAccountResponseDTO(account);
    }

    private Account findById(UUID id){
        return accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found with ID:" + id));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
