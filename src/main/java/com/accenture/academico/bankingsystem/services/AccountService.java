package com.accenture.academico.bankingsystem.services;
import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.dtos.account.AccountRequestDTO;
import com.accenture.academico.bankingsystem.dtos.account.AccountResponseDTO;
import com.accenture.academico.bankingsystem.dtos.account.AccountTransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.account.AccountUpdateDTO;
import com.accenture.academico.bankingsystem.exceptions.AmountNegativeException;
import com.accenture.academico.bankingsystem.exceptions.ConflictException;
import com.accenture.academico.bankingsystem.exceptions.InsufficientFundsException;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.mappers.account.AccountMapper;
import com.accenture.academico.bankingsystem.middlewares.AccountNumberGenerator;
import com.accenture.academico.bankingsystem.middlewares.UserTools;
import com.accenture.academico.bankingsystem.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientService clientService;
    private final AgencyService agencyService;

    public List<AccountResponseDTO> getAllAccounts(){
        return AccountMapper.convertToAccountResponseDTOList(accountRepository.findAll());
    }

    public AccountResponseDTO getAccountById(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return AccountMapper.convertToAccountResponseDTO(account);
    }

    public AccountResponseDTO createAccount(AccountRequestDTO requestDTO) {
        List<Account> accounts = getMyAccounts();

        if(!accounts.isEmpty()){
            accounts.stream()
                    .filter(account -> account.getAccountType().equals(requestDTO.accountType()))
                    .findFirst()
                    .ifPresent(account -> {
                        throw new ConflictException("Account type already exists");
                    });
        }

        Account account = new Account();
        account.setNumber(AccountNumberGenerator.generateAccountNumber());
        account.setAccountType(requestDTO.accountType());
        account.setAgency(agencyService.getAgencyInternalById(requestDTO.agencyId()));
        account.setBalance(BigDecimal.ZERO);
        account.setClient(getMyClient());
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.convertToAccountResponseDTO(savedAccount);
    }

    public AccountResponseDTO updateAccount(UUID id, AccountUpdateDTO requestDTO) {
        Account account = getById(id);
        List<Account> myAccounts = getMyAccounts();

        if(requestDTO.accountType() != null){

            myAccounts.stream()
                    .filter(myAccount -> account.getAccountType().equals(requestDTO.accountType()))
                    .findFirst()
                    .ifPresent(myAccount -> {
                        throw new ConflictException("Account type already exists");
                    });

            account.setAccountType(requestDTO.accountType());
        }

        if(requestDTO.agencyId() != null){
            Agency agency = agencyService.findById(requestDTO.agencyId());

            myAccounts.stream()
                    .filter(myAccount -> myAccount.getAgency().getId().equals(agency.getId()))
                    .peek(myAccount -> {
                        if (myAccount.getAccountType().equals(requestDTO.accountType())) {
                            throw new ConflictException("Account type already exists in the selected agency");
                        }
            });

            account.setAgency(agency);
        }

        Account updatedAccount = accountRepository.save(account);
        return AccountMapper.convertToAccountResponseDTO(updatedAccount);
    }

    public void deleteAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        accountRepository.delete(account);
    }

    public List<Account> getAccountsByClientId(UUID id){
        return accountRepository.findByClientId(id);
    }

    public List<Account> getMyAccounts(){
        return getAccountsByClientId(getMyClient().getId());
    }

    public Client getMyClient(){
        return clientService.findByUser(UserTools.getUserContextId());
    }

    public Account getById(UUID id){
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agency not found"));
    }

    public AccountTransactionResponseDTO deposit(UUID accountId, BigDecimal amount) {
        Account account = getById(accountId);
        validateAmount(amount);

        account.setBalance(account.getBalance().add(amount));
        Account updatedAccount = accountRepository.save(account);
        return AccountMapper.convertToAccountTransactionResponseDTO(updatedAccount, TransactionType.DEPOSIT, amount);
    }

    public AccountTransactionResponseDTO sac(UUID accountId, BigDecimal amount) {
        validateAmount(amount);

        Account account = getById(accountId);

        validateSufficientFunds(account,amount);

        account.setBalance(account.getBalance().subtract(amount));
        Account updatedAccount = accountRepository.save(account);
        return AccountMapper.convertToAccountTransactionResponseDTO(updatedAccount, TransactionType.SAC, amount);

    }

    public AccountTransactionResponseDTO transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount) {
        validateAmount(amount);

        Account fromAccount = getById(fromAccountId);
        Account toAccount = getById(toAccountId);

        validateSufficientFunds(fromAccount, amount);

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

       Account myAccount = accountRepository.save(fromAccount);
       accountRepository.save(toAccount);

       return AccountMapper.convertToAccountTransactionResponseDTO(myAccount, TransactionType.TRANSFER, amount);

    }


    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AmountNegativeException("Amount must be greater than zero");
        }
    }

    private void validateSufficientFunds(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }

}
