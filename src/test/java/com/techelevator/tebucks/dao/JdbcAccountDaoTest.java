package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class JdbcAccountDaoTest extends BaseDaoTests  {
    private JdbcAccountDao sut;
    private static final Account ACCOUNT_1 = new Account(1, 1, new BigDecimal(1000));
    private static final Account ACCOUNT_2 = new Account(2, 2, new BigDecimal(1000));
    private static final Account ACCOUNT_3 = new Account(3, 3, new BigDecimal(1000));

    @Before
    public void setup() { JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate.getDataSource());}


    @Test
    public void getAccounts_returns_correct_number_of_accounts() {
        List<Account> accounts = sut.getAccounts();
        assertEquals(3, accounts.size());
    }

    @Test
    public void getAccountById_returns_correct_account() {
        Account account = sut.getAccountById(1);
        assertNotNull(account);
        assertEquals(account.getAccountId(), 1);

        Account account2 = sut.getAccountById(2);
        assertNotNull(account2);
        assertEquals(account2.getAccountId(), 2);
    }

    @Test
    public void getAccountByUserId_returns_correct_account() {
        Account account = sut.getAccountByUserId(1);
        assertNotNull(account);
        assertEquals(account.getUserId(), 1);
    }

    @Test
    public void createAccount_creates_an_account() {
        Account account = new Account(3, 3, new BigDecimal(1000));
        Account createdAccount = sut.createAccount(account);
        assertNotNull(createdAccount);
    }

    @Test
    public void getAccountByUserName_returns_correct_account() {
        Account account = sut.getAccountByUserName("user1");
        assertNotNull(account);
        assertEquals(account.getUserId(), 1);
    }

    @Test
    public void updateBalance() {
        Account account = sut.getAccountById(1);
        account.setBalance(new BigDecimal(2000));
        sut.updateBalance(account);
        assertEquals(account.getBalance(), new BigDecimal(2000));
    }

    @Test
    public void deleteAccount() {
        sut.deleteAccount(1);
        assertNull(sut.getAccountById(1));
    }
}