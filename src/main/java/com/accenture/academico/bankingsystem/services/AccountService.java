//package com.accenture.academico.bankingsystem.services;
//import com.accenture.academico.bankingsystem.domain.account.Account;
//import com.accenture.academico.bankingsystem.domain.client.Client;
//import com.accenture.academico.bankingsystem.dtos.account.AccountRequestDTO;
//import com.accenture.academico.bankingsystem.dtos.account.AccountResponseDTO;
//import com.accenture.academico.bankingsystem.exceptions.ConflictException;
//import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
//import com.accenture.academico.bankingsystem.mappers.account.AccountMapper;
//import com.accenture.academico.bankingsystem.middlewares.AccountNumberGenerator;
//import com.accenture.academico.bankingsystem.middlewares.UserTools;
//import com.accenture.academico.bankingsystem.repositories.AccountRepository;
//import com.accenture.academico.bankingsystem.repositories.ClientRepository;
//import com.accenture.academico.bankingsystem.repositories.AgencyRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class AccountService {
//
//    private final AccountRepository accountRepository;
//    private final ClientService clientService;
//    private final AgencyService agencyService;
//
//    public List<AccountResponseDTO> getAllAccounts(){
//        return AccountMapper.convertToAccountResponseDTOList(accountRepository.findAll());
//    }
//
//    public AccountResponseDTO getAccountById(UUID id) {
//        Account account = accountRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Account not found"));
//        return AccountMapper.convertToAccountResponseDTO(account);
//    }
//
//    public AccountResponseDTO createAccount(AccountRequestDTO requestDTO) {
//        List<Account> accounts = getMyAccounts();
//
//        if(!accounts.isEmpty()){
//            accounts.stream()
//                    .filter(account -> account.getAccountType() == requestDTO.accountType())
//                    .findFirst()
//                    .ifPresent(account -> {
//                        throw new ConflictException("Account type already exists");
//                    });
//        }
//
//        Account account = new Account();
//        account.setNumber(AccountNumberGenerator.generateAccountNumber());
//        account.setAccountType(requestDTO.accountType());
//        account.setAgency(agencyRepository.findById(requestDTO.getAgencyId())
//                .orElseThrow(() -> new RuntimeException("Agency not found")));
//        account.setBalance(BigDecimal.ZERO); // Default balance
//        account.setClient(clientRepository.findById(requestDTO.getClientId())
//                .orElseThrow(() -> new RuntimeException("Client not found")));
//
//        Account savedAccount = accountRepository.save(account);
//        return toDto(savedAccount);
//    }
//
//    public AccountResponseDTO updateAccount(UUID id, AccountRequestDTO requestDTO) {
//        Account account = accountRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        account.setNumber(AccountNumberGenerator.generateAccountNumber());
//        account.setAccountType(requestDTO.accountType());
//        account.setAgency(agencyRepository.findById(requestDTO.getAgencyId())
//                .orElseThrow(() -> new RuntimeException("Agency not found")));
//        account.setClient(clientRepository.findById(requestDTO.getClientId())
//                .orElseThrow(() -> new RuntimeException("Client not found")));
//
//        Account updatedAccount = accountRepository.save(account);
//        return toDto(updatedAccount);
//    }
//
//    public void deleteAccount(UUID id) {
//        Account account = accountRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//        accountRepository.delete(account);
//    }
//
//    public List<Account> getAccountsByClientId(UUID id){
//        return accountRepository.findByClientId(id);
//    }
//
//    public List<Account> getMyAccounts(){
//        Client client = clientService.findByUser(UserTools.getUserContextId());
//        return getAccountsByClientId(client.getId());
//    }
//
//    public Account getById(UUID id){
//        return accountRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Agency not found"));
//    }
//}
