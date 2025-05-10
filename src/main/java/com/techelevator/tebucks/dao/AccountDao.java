package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;

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
