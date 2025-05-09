package com.techelevator.tebucks.security.dao;

import com.techelevator.tebucks.security.model.Account;

import java.util.List;

public interface AccountDao {
    List<Account> getAccounts();

    Account getAccountById(int id);

    Account getAccountByUserId(int id);

    Account createAccount(Account account);

    Account getAccountByUserName(String username);

    Account updateBalance(Account account);

    int deleteAccount(int id);
}
