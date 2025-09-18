package com.app.service.impl;

import com.app.persistence.model.Account;
import com.app.persistence.repository.IAccountRepository;
import com.app.presentation.dto.AccountDTO;
import com.app.presentation.dto.UpdateAccountDTO;
import com.app.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private final IAccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<AccountDTO> findAll() {
        List<Account> accounts = this.accountRepository.findAll();

        return accounts.stream()
                       .map(account -> this.modelMapper.map(account, AccountDTO.class))
                       .toList();
    }

    @Override
    public AccountDTO save(AccountDTO request) {
        Account newAccount = this.modelMapper.map(request, Account.class);

        Account accountSaved = this.accountRepository.save(newAccount);

        return this.modelMapper.map(accountSaved, AccountDTO.class);
    }

    @Override
    public Optional<AccountDTO> update(String accountNumber, UpdateAccountDTO request) {
        return this.accountRepository.findById(accountNumber)
                                     .map(account -> {
                                         account.setAccountType(request.accountType());
                                         account.setStatus(request.status());

                                         Account updated = this.accountRepository.save(account);
                                         return this.modelMapper.map(updated, AccountDTO.class);
                                    });
    }

    @Override
    public boolean delete(String accountNumber) {
        return this.accountRepository.findById(accountNumber)
                                     .map(account -> {
                                         this.accountRepository.delete(account);
                                         return true;
                                     })
                                     .orElse(false);
    }
}
