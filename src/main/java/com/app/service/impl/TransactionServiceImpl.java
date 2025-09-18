package com.app.service.impl;

import com.app.persistence.model.*;
import com.app.persistence.repository.IAccountRepository;
import com.app.persistence.repository.ITransactionRepository;
import com.app.presentation.dto.SaveTransactionDTO;
import com.app.presentation.dto.TransactionDTO;
import com.app.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {
    private final ITransactionRepository transactionRepository;
    private final IAccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<TransactionDTO> findAll() {
        List<Transaction> transactions = this.transactionRepository.findAll();

        return transactions.stream()
                           .map(transaction -> this.modelMapper.map(transaction, TransactionDTO.class))
                           .toList();
    }

    @Override
    @Transactional
    public Optional<TransactionDTO> save(SaveTransactionDTO request) {
        return this.accountRepository.findById(request.accountNumber())
                                     .map(account -> {
                                         BigDecimal newBalance = calculateBalance(request, account);
                                         account.setInitialBalance(newBalance);

                                         Transaction newTransaction = Transaction.builder()
                                                                                 .transactionType(request.transactionType())
                                                                                 .amount(request.amount())
                                                                                 .account(account)
                                                                                 .balance(account.getInitialBalance())
                                                                                 .build();

                                         Transaction transactionSaved = this.transactionRepository.save(newTransaction);

                                         return this.modelMapper.map(transactionSaved, TransactionDTO.class);
                                     });
    }

    private static BigDecimal calculateBalance(SaveTransactionDTO request, Account account) {
        BigDecimal newBalance;

        if (request.transactionType() == ETransactionType.DEPOSIT) {
            newBalance = account.getInitialBalance().add(request.amount());
        } else {
            if (account.getInitialBalance().compareTo(request.amount()) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente para realizar el retiro");
            }

            newBalance = account.getInitialBalance().subtract(request.amount());
        }

        return newBalance;
    }
}
