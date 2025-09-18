package com.app.persistence.repository;

import com.app.persistence.model.Transaction;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ITransactionRepository extends ListCrudRepository<Transaction, UUID> { }
