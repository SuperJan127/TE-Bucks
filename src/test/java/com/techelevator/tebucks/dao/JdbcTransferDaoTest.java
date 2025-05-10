package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.security.dao.*;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.security.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

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

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userDao = new JdbcUserDao(jdbcTemplate, accountDao);
        sut = new JdbcTransferDao(jdbcTemplate, userDao, accountDao);
    }


    @Test
    public void getTransfers_returns_correct_amount_of_transfers() {
        List<Transfer> transfers= sut.getTransfers();
        assertEquals(transfers.size(), 3);
    }

    @Test
    public void getTransferByTransferId_returns_a_transfer() {
        Transfer transfer = sut.getTransferByTransferId(1);
        assertNotNull(transfer);
    }

    @Test
    public void getTransfersByAccountId_returns_correct_amount_of_transfers() {
        List<Transfer> transfers = sut.getTransfersByAccountId(2);
        assertEquals(transfers.size(), 2);
    }

    @Test
    public void getTransferByStatusId() {
        List<Transfer> transfers = sut.getTransferByStatusId(1);
        assertEquals(2, transfers.size());
    }

    @Test
    public void updateTransferStatus() {
        Transfer transfer = sut.getTransferByTransferId(2);
        transfer.setTransferStatus("Pending");
        sut.updateTransferStatus(transfer);
        assertEquals("Pending", transfer.getTransferStatus());
    }

    @Test
    public void deleteTransfer() {
        sut.deleteTransfer(1);
        assertNull(sut.getTransferByTransferId(1));
    }

    @Test
    public void createTransfer() {
        Transfer transfer = new Transfer(10, "Send","Pending",USER_1,USER_2,new BigDecimal(100));
        assertNotNull(transfer);
    }
}