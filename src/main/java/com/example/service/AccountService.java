package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;

import com.example.entity.Account;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.exception.InvalidLoginCredentialsException;

@Service
public class AccountService {

    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account account) {

        if (account.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }

        if (account.getPassword().isBlank() || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be greater than four characters long.");
        }

        Account takenAccount = accountRepository.findByUsername(account.getUsername());

        if (takenAccount != null) {
            throw new UsernameAlreadyExistsException("Username already exists, please choose a new one.");
        }

        return accountRepository.save(account);

    }

    public Account login(Account account) {

        Account existingAccount = accountRepository.findByUsername(account.getUsername());

        if (existingAccount == null) {
            throw new InvalidLoginCredentialsException("Cannot find an account with that username");
        }

        if (!existingAccount.getPassword().equals(account.getPassword())) {
            throw new InvalidLoginCredentialsException("Incorrect password");
        }

        return existingAccount;

    }

}
