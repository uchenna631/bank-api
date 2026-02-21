package com.example.bank_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.bank_api.model.Account;

public interface AccountRepository extends MongoRepository<Account, String> {
}
