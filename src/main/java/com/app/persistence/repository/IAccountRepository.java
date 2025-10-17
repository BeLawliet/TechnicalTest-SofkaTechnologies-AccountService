package com.app.persistence.repository;

import com.app.persistence.model.Account;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IAccountRepository extends ListCrudRepository<Account, String> {
    Optional<Account> findByCustomerId(UUID customerId);
}
