package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.security.dao.AccountDao;
import com.techelevator.tebucks.security.dao.JdbcAccountDao;
import com.techelevator.tebucks.security.dao.JdbcTransferDao;
import com.techelevator.tebucks.security.dao.UserDao;
import com.techelevator.tebucks.security.model.Account;
import com.techelevator.tebucks.security.model.Transfer;
import com.techelevator.tebucks.security.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcTransferDaoTest extends BaseDaoTests {

    private JdbcTransferDao sut;

    private UserDao userDao;

    private AccountDao accountDao;

    protected static final User USER_1 = new User(1, "user1", "user1", "ROLE_USER", true);
    protected static final User USER_2 = new User(2, "user2", "user2", "ROLE_USER", true);
    private static final User USER_3 = new User(3, "user3", "user3", "ROLE_USER", true);

    private static final Transfer TRANSFER_1 = new Transfer(1, "Send", "Approved", USER_1, USER_2, new BigDecimal(20));
    private static final Transfer TRANSFER_2 = new Transfer(2, "Request", "Pending", USER_2, USER_1, new BigDecimal(22.25));
    private static final Transfer TRANSFER_3 = new Transfer(3, "Request", "Rejected", USER_3, USER_1, new BigDecimal(30));

    @BeforeEach
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource); sut = new JdbcTransferDao(jdbcTemplate, userDao, accountDao);}


    @Test
    void getTransfers() {
    }

    @Test
    void getTransferByTransferId() {
    }

    @Test
    void getTransfersByAccountId() {
    }

    @Test
    void getTransferByStatusId() {
    }

    @Test
    void updateTransferStatus() {
    }

    @Test
    void deleteTransfer() {
    }

    @Test
    void createTransfer() {
    }
}