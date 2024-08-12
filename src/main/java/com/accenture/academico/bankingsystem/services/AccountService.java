package com.accenture.academico.bankingsystem.services;
import com.accenture.academico.bankingsystem.domain.account.Account;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.domain.client.Client;
import com.accenture.academico.bankingsystem.domain.enums.AccountType;
import com.accenture.academico.bankingsystem.domain.enums.TransactionType;
import com.accenture.academico.bankingsystem.domain.pix_key.PixKey;
import com.accenture.academico.bankingsystem.dtos.account.AccountRequestDTO;
import com.accenture.academico.bankingsystem.dtos.account.AccountResponseDTO;
import com.accenture.academico.bankingsystem.dtos.pix_key.PixRequestDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionResponseDTO;
import com.accenture.academico.bankingsystem.dtos.account.AccountUpdateDTO;
import com.accenture.academico.bankingsystem.dtos.transaction.TransactionTransferResponseDTO;
import com.accenture.academico.bankingsystem.exceptions.AmountNegativeException;
import com.accenture.academico.bankingsystem.exceptions.ConflictException;
import com.accenture.academico.bankingsystem.exceptions.InsufficientFundsException;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.mappers.account.AccountMapper;
import com.accenture.academico.bankingsystem.mappers.transaction.TransactionMapper;
import com.accenture.academico.bankingsystem.middlewares.AccountNumberGenerator;
import com.accenture.academico.bankingsystem.middlewares.UserTools;
import com.accenture.academico.bankingsystem.repositories.AccountRepository;
import com.accenture.academico.bankingsystem.repositories.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientService clientService;
    private final AgencyService agencyService;
    private final PixKeyRepository pixKeyRepository;

    public List<AccountResponseDTO> getAllAccounts(){
        return AccountMapper.convertToAccountResponseDTOList(accountRepository.findAll());
    }

    public AccountResponseDTO getAccountById(UUID id) {
        log.debug("Fetching account with ID: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return AccountMapper.convertToAccountResponseDTO(account);
    }

    public AccountResponseDTO createAccount(AccountRequestDTO requestDTO) {
        log.debug("Creating a new account for client");

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
        log.debug("Account created successfully with ID: {}", savedAccount.getId());

        return AccountMapper.convertToAccountResponseDTO(savedAccount);
    }

    public AccountResponseDTO updateAccount(UUID id, AccountUpdateDTO requestDTO) {
        log.debug("Updating account with ID: {}", id);

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

        log.debug("Account updated successfully with ID: {}", updatedAccount.getId());

        return AccountMapper.convertToAccountResponseDTO(updatedAccount);
    }

    public void deleteAccount(UUID id) {
        log.debug("Deleting account with ID: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        accountRepository.delete(account);

        log.debug("Account deleted successfully with ID: {}", id);

    }

    public List<Account> getAccountsByClientId(UUID id){
        return accountRepository.findByClientId(id);
    }

    public List<Account> getMyAccounts(){
        return getAccountsByClientId(getMyClient().getId());
    }

    private Client getMyClient(){
        return clientService.findByUser(UserTools.getUserContextId());
    }

    public Account getById(UUID id){
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agency not found"));
    }

    public TransactionResponseDTO deposit(UUID accountId, BigDecimal amount) {
        log.debug("Depositing amount {} to account ID: {}", amount, accountId);

        Account account = getById(accountId);
        validateAmount(amount);

        account.setBalance(account.getBalance().add(amount));
        Account updatedAccount = accountRepository.save(account);

        log.debug("Deposit successful. Updated balance: {}", updatedAccount.getBalance());

        return TransactionMapper.convertToTransactionResponseDTO(updatedAccount, TransactionType.DEPOSIT, amount);
    }

    public TransactionResponseDTO sac(UUID accountId, BigDecimal amount) {
        log.debug("Withdrawing amount {} from account ID: {}", amount, accountId);

        validateAmount(amount);

        Account account = getById(accountId);

        validateSufficientFunds(account,amount);

        account.setBalance(account.getBalance().subtract(amount));
        Account updatedAccount = accountRepository.save(account);

        log.debug("Withdrawal successful. Updated balance: {}", updatedAccount.getBalance());

        return TransactionMapper.convertToTransactionResponseDTO(updatedAccount, TransactionType.SAC, amount);

    }

    public TransactionTransferResponseDTO transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount) {
        log.debug("Transferring amount {} from account ID: {} to account ID: {}", amount, fromAccountId, toAccountId);

        validateAmount(amount);

        Account fromAccount = getById(fromAccountId);
        Account toAccount = getById(toAccountId);

        validateSufficientFunds(fromAccount, amount);

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        log.debug("Transfer successful. Updated balances - From Account: {}, To Account: {}", fromAccount.getBalance(), toAccount.getBalance());

        return TransactionMapper.convertToTransactionTransferResponseDTO(fromAccount,toAccount,amount,TransactionType.TRANSFER);
    }

    public TransactionTransferResponseDTO pix(PixRequestDTO pixDTO){
        log.debug("Processing PIX transaction for amount: {}", pixDTO.value());

        validateAmount(pixDTO.value());

        Account senderAccount = findByClientIdAndAccountType(getMyClient().getId(), pixDTO.accountType());
        Account receiverAccount = getById(
                getPixKeyByKeyValue(pixDTO.pixKey())
                .getAccount().getId());

        validateSufficientFunds(senderAccount, pixDTO.value());

        senderAccount.setBalance(senderAccount.getBalance().subtract(pixDTO.value()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(pixDTO.value()));

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        log.debug("PIX transaction successful. Updated balances - Sender Account: {}, Receiver Account: {}", senderAccount.getBalance(), receiverAccount.getBalance());

        return TransactionMapper.convertToTransactionTransferResponseDTO(senderAccount,receiverAccount,pixDTO.value(),TransactionType.PIX);
    }

    private PixKey getPixKeyByKeyValue(String keyValue){
        return pixKeyRepository.findByKeyValue(keyValue).orElseThrow(() -> new NotFoundException("PixKey not found with keyValue: " + keyValue));
    }

    private Account findByClientIdAndAccountType(UUID id, AccountType accountType){
        return accountRepository.findByClientIdAndAccountType(id, accountType).orElseThrow(() ->
                new NotFoundException("Account not found with clientId: "+ id +" and accountType: "+ accountType));
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
