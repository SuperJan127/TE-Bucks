package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.security.dao.JdbcAccountDao;
import com.techelevator.tebucks.security.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;*/

public class JdbcAccountDaoTest1 extends BaseDaoTests  {
    private JdbcAccountDao sut;
    private static final Account ACCOUNT_1 = new Account(1, 1, new BigDecimal(1000));
    private static final Account ACCOUNT_2 = new Account(2, 2, new BigDecimal(1000));
    private static final Account ACCOUNT_3 = new Account(3, 3, new BigDecimal(1000));

    @BeforeEach
    public void setup() { JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    sut = new JdbcAccountDao(jdbcTemplate.getDataSource());}


    @Test
    void getAccounts_returns_correct_number_of_accounts() {
        List<Account> accounts = sut.getAccounts();
        assertEquals(3, accounts.size());
    }

    @Test
    void getAccountById_returns_correct_account() {
        Account account = sut.getAccountById(1);
        //assertEquals
    }

    @Test
    void getAccountByUserId() {
    }

    @Test
    void createAccount() {
    }

    @Test
    void getAccountByUserName() {
    }

    @Test
    void updateBalance() {
    }

    @Test
    void deleteAccount() {
    }
}