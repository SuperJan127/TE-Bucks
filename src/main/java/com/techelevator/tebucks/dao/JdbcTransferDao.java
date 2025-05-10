package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.security.dao.UserDao;
import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.security.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private final JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, UserDao userDao, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @Override
    public List<Transfer> getTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select transfer_id, account_from, account_to, amount, type_name, " +
                "status_name " +
                "from transfers t " +
                "join transfer_types tt on t.transfer_type_id = tt.type_id " +
                "join transfer_statuses ts on t.transfer_status_id = ts.status_id;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()){
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(int id) {
        Transfer transfer = null;
        String sql = "select transfer_id, account_from, account_to, amount, type_name, status_name \n" +
                "from transfers t " +
                "join transfer_types tt on t.transfer_type_id = tt.type_id " +
                "join transfer_statuses ts on t.transfer_status_id = ts.status_id " +
                "where transfer_id = ?;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if(results.next()){
                transfer = mapRowToTransfer(results);
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getTransfersByAccountId(int account_id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select transfer_id, account_from, account_to, amount, type_name, status_name " +
                "from transfers t " +
                "join transfer_types tt on t.transfer_type_id = tt.type_id " +
                "join transfer_statuses ts on t.transfer_status_id = ts.status_id " +
                "where account_to = ? or account_from = ? ;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_id, account_id);
            while (results.next()){
                transfers.add(mapRowToTransfer(results));
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransferByStatusId(int status_id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select transfer_id, account_from, account_to, amount, type_name, status_name " +
                "from transfers t " +
                "join transfer_types tt on t.transfer_type_id = tt.type_id " +
                "join transfer_statuses ts on t.transfer_status_id = ts.status_id " +
                "where status_id = ?;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, status_id);
            while(results.next()){
                transfers.add(mapRowToTransfer(results));
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public Transfer updateTransferStatus(Transfer transfer) {
        Transfer output = null;
        String sql = "update transfers" +
                " set transfer_status_id = ? where transfer_id = ?;";
        int statusId = 1;
        if (transfer.getTransferStatus().equalsIgnoreCase("Approved")){
            statusId = 2;
        } else if (transfer.getTransferStatus().equalsIgnoreCase("Rejected")){
            statusId = 3;
        }
        try {
            int numberOfRows = jdbcTemplate.update(sql, statusId, transfer.getTransferId());
            if (numberOfRows == 0){
                throw new DaoException("No transfers were updated");
            }else {
                output = getTransferByTransferId(transfer.getTransferId());
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }catch (Exception e) {
            throw new DaoException("Cannot update transfer",e);
        }
        return output;
    }

    @Override
    public int deleteTransfer(int id) {
        int numberOfRows = 0;
        String sql = "delete from transfers where transfer_id = ?;";
        try {
            numberOfRows = jdbcTemplate.update(sql, id);
        } catch (Exception e){
            throw new DaoException("Cannot delete transfer", e);
        }
        return numberOfRows;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        Transfer output =null;
        String sql = "insert into transfers (transfer_type_id, transfer_status_id, account_from, " +
                "account_to, amount) values (?, ?, ?, ?, ?) returning transfer_id;";
        int status = 1;
        if(transfer.getTransferStatus().equalsIgnoreCase("Approved")){
            status = 2;
        } else if (transfer.getTransferStatus().equalsIgnoreCase("Rejected")){
            status = 3;
        }
        int type = 1;
        if(transfer.getTransferType().equalsIgnoreCase("Send")){
            type = 2;
        }

        Account accountTo = accountDao.getAccountByUserId(transfer.getUserTo().getId());
        Account accountFrom = accountDao.getAccountByUserId(transfer.getUserFrom().getId());
        try {
            int newTransferId = jdbcTemplate.queryForObject(sql, int.class, type, status,
                    accountFrom.getAccountId(), accountTo.getAccountId(), transfer.getAmount());
            output = getTransferByTransferId(newTransferId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Can't get a reference to the source of data.",e);
        } catch(DataIntegrityViolationException e){
            throw new DaoException("Data breaks the rules.",e);
        }
        return output;
    }
    
    public Transfer mapRowToTransfer(SqlRowSet rowSet){
        User userTo = userDao.getUserById(rowSet.getInt("account_to"));
        User userFrom = userDao.getUserById(rowSet.getInt("account_from"));
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferType(rowSet.getString("type_name"));
        transfer.setTransferStatus(rowSet.getString("status_name"));
        transfer.setUserFrom(userFrom);
        transfer.setUserTo(userTo);
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }
}
