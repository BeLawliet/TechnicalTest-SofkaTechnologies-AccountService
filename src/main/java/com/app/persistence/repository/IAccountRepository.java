package com.app.persistence.repository;

import com.app.persistence.model.Account;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends ListCrudRepository<Account, String> {}
