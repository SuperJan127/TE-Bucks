package com.techelevator.tebucks.security.dao;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.security.model.Account;
import com.techelevator.tebucks.security.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Transfer> getTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select * from transfers;";
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
        String sql = "select * from transfers where transfer_id = ?;";
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
        String sql = "select * from transfers where account_to = ? OR account_from = ?";
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
    public List<Transfer> getTransfersByAccountTo(int account_id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select * from transfers where account_to = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_id);
            while (results.next()){
                transfers.add(mapRowToTransfer(results));
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransferByAccountFrom(int account_id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select * from transfers where account_from = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_id);
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
        String sql = "select * from transfers where status_id = ?;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, status_id);
            if(results.next()){
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
                "set transfer_status where transfer_id = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(sql, transfer.getTransferId());
            if (numberOfRows == 0){
                throw new DaoException("No parks were updated");
            }else {
                output = getTransferByTransferId(transfer.getTransferId());
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }catch (Exception e) {
            throw new DaoException("Cannot update account",e);
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
                "account_from, amount) values (?, ?, ?, ?, ?);";

        try {
            int newTransferId = jdbcTemplate.queryForObject(sql, int.class,
                    transfer.getTransferTypeId(),transfer.getTransferStatusId(),
                    transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
            output = getTransferByTransferId(newTransferId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Can't get a reference to the source of data.",e);
        } catch(DataIntegrityViolationException e){
            throw new DaoException("Data breaks the rules.",e);
        }
        return output;
    }
    
    public Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("accountTo"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }
}
