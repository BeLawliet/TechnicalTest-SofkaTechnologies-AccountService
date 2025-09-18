package com.app.service;

import com.app.presentation.dto.AccountDTO;
import com.app.presentation.dto.UpdateAccountDTO;
import java.util.List;
import java.util.Optional;

public interface IAccountService {
    List<AccountDTO> findAll();
    AccountDTO save(AccountDTO request);
    Optional<AccountDTO> update(String accountNumber, UpdateAccountDTO request);
    boolean delete(String accountNumber);
}
