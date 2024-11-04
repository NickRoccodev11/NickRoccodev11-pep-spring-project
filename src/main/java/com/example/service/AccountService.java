package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;

import java.util.List;
import java.util.ArrayList;

import com.example.entity.Account;

@Service
public class AccountService {

    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account account) {

        if (account.getUsername().isBlank()) {
            return null;
        }

        if (account.getPassword().isBlank() || account.getPassword().length() < 4) {
            return null;
        }

        return accountRepository.save(account);

    }

    public Account login(Account account) {

        Account existingAccount = accountRepository.findByUsername(account.getUsername());

        if (existingAccount == null || !existingAccount.getPassword().equals(account.getPassword())) {
            return null;
        }

        return existingAccount;

    }

    public Account getAccountByUsername(String userName) {
        return accountRepository.findByUsername(userName);
    }

}
