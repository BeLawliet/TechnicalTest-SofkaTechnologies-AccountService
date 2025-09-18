package com.app.service;

import com.app.presentation.dto.SaveTransactionDTO;
import com.app.presentation.dto.TransactionDTO;
import java.util.List;
import java.util.Optional;

public interface ITransactionService {
    List<TransactionDTO> findAll();
    Optional<TransactionDTO> save(SaveTransactionDTO request);
}
