package com.example.bank_api.controller;

import com.example.bank_api.exception.AccountNotFoundException;
import com.example.bank_api.model.Account;
import com.example.bank_api.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST Controller for managing bank accounts.
 * 
 * Provides endpoints for creating accounts, performing deposits and withdrawals,
 * viewing account details, and managing account information.
 * 
 * All endpoints are prefixed with "/accounts" and return Account objects or collections thereof.
 * 
 * Validation rules:
 * - Owner names cannot be null or empty
 * - Transaction amounts must be at least 0.01
 * - Account balance cannot go below 0.0
 * 
 * @see Account
 * @see AccountRepository
 * @see AccountNotFoundException
 * @CrossOrigin(origins = "http://localhost:3000") for allowing cross-origin requests from a frontend application running on localhost:3000
 */
@CrossOrigin(origins = "")
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository repo;
    private static final double MINIMUM_BALANCE = 0.0;
    private static final double MIN_TRANSACTION_AMOUNT = 0.01;

    public AccountController(AccountRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Account create(@RequestParam String owner, @RequestParam(required = false) Double openingBalance) {
        validateOwnerName(owner);
        if (openingBalance == null) {
            openingBalance = MINIMUM_BALANCE;
        }
        Account account = new Account(owner, openingBalance);
        return repo.save(account);
    }

    @PostMapping("/{id}/deposit")
    public Account deposit(@PathVariable String id, @RequestParam double amount) {
        validateTransactionAmount(amount);
        
        Account account = getAccountById(id);
        account.setBalance(account.getBalance() + amount);
        return repo.save(account);
    }

    @PostMapping("/{id}/withdraw")
    public Account withdraw(@PathVariable String id, @RequestParam double amount) {
        validateTransactionAmount(amount);
        
        Account account = getAccountById(id);
        double newBalance = account.getBalance() - amount;
        
        if (newBalance < MINIMUM_BALANCE) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Insufficient funds. Current balance: " + account.getBalance() + ", Requested withdrawal: " + amount
            );
        }
        
        account.setBalance(newBalance);
        return repo.save(account);
    }

    @PostMapping("/{id}/delete")
    public void deleteAccount(@PathVariable String id) {
        Account account = getAccountById(id);
        repo.delete(account);
    }

    @GetMapping("/{id}/balance")
    public double balance(@PathVariable String id) {
        return getAccountById(id).getBalance();
    }

    @GetMapping
    public Iterable<Account> listAccounts() {
        return repo.findAll();
    }

    @PostMapping("/{id}/rename")
    public Account renameOwner(@PathVariable String id, @RequestParam String newOwner) {
        validateOwnerName(newOwner);
        Account account = getAccountById(id);
        account.setOwner(newOwner);
        return repo.save(account);
    }

    // Helper methods
    private Account getAccountById(String id) {
        return repo.findById(id)
            .orElseThrow(() -> new AccountNotFoundException(id));
    }

    private void validateOwnerName(String owner) {
        if (owner == null || owner.trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Owner name cannot be empty"
            );
        }
    }

    private void validateTransactionAmount(double amount) {
        if (amount < MIN_TRANSACTION_AMOUNT) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Transaction amount must be greater than " + MIN_TRANSACTION_AMOUNT
            );
        }
    }
}