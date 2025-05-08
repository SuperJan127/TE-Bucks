package com.techelevator.tebucks.security.services;

import com.techelevator.tebucks.security.dao.AccountDao;
import com.techelevator.tebucks.security.dao.TransferDao;
import com.techelevator.tebucks.security.dao.UserDao;
import com.techelevator.tebucks.security.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;

    public AccountService(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    public void updateBalance(Account account, BigDecimal amount, boolean isAddition) {
        BigDecimal newBalance;
        if (isAddition) {
            newBalance = account.getBalance().add(amount);
        } else {
            newBalance = account.getBalance().subtract(amount);
        }
        account.setBalance(newBalance);
        accountDao.updateBalance(account);
    }
}
