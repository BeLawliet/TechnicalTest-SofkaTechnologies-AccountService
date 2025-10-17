package com.app.service.impl;

import com.app.persistence.model.Account;
import com.app.persistence.model.EAccountType;
import com.app.persistence.repository.IAccountRepository;
import com.app.presentation.dto.AccountDTO;
import com.app.presentation.dto.CustomerEventDTO;
import com.app.presentation.dto.ETypeEventDTO;
import com.app.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private static final SecureRandom random = new SecureRandom();
    private final IAccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @KafkaListener(topics = "banking-topic", groupId = "account-service-group")
    public void getEvent(CustomerEventDTO event) {
        if (event.typeEvent().equals(ETypeEventDTO.CUSTOMER_CREATED)) {
            this.save(event);
        } else {
            this.updateAndDelete(event);
        }
    }

    @Override
    public List<AccountDTO> findAll() {
        List<Account> accounts = this.accountRepository.findAll();

        return accounts.stream()
                       .map(account -> this.modelMapper.map(account, AccountDTO.class))
                       .toList();
    }

    @Override
    public void save(CustomerEventDTO event) {
        String accountNumber = "000000000" + (random.nextInt(90) + 10);

        Account newAccount = Account.builder()
                                    .accountNumber(accountNumber)
                                    .accountType(EAccountType.valueOf(event.accountType()))
                                    .initialBalance(BigDecimal.ZERO)
                                    .customerId(event.customerId())
                                    .status(event.status())
                                    .build();

        this.accountRepository.save(newAccount);
    }

    @Override
    public void updateAndDelete(CustomerEventDTO event) {
        this.accountRepository.findByCustomerId(event.customerId())
                              .map(account -> {
                                    account.setStatus(event.status());
                                    return this.accountRepository.save(account);
                              })
                              .orElseThrow(() -> new IllegalArgumentException("El cliente indicado no tiene cuentas asociadas"));
    }
}
