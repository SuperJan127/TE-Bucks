package com.techelevator.tebucks.security.dao;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.security.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "select account_id, user_id, balance from accounts;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()){
                accounts.add(mapRowToAccount(results));
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return accounts;
    }

    @Override
    public Account getAccountById(int id) {
        Account account = null;
        String sql = "select account_id, user_id, balance from accounts where account_id = ?;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if(results.next()){
                account = mapRowToAccount(results);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
         return account;
    }

    @Override
    public Account getAccountByUserId(int id) {
        Account account = null;
        String sql = "select account_id, user_id, balance from accounts where user_id = ?;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if(results.next()){
                account = mapRowToAccount(results);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return account;
    }

    @Override
    public Account createAccount(Account account) {
        Account output =null;
        String sql = "insert into accounts(user_id, balance) values (?, 1000) returning " +
                "account_id;";
        try {
            int newAccountId = jdbcTemplate.queryForObject(sql, int.class, account.getUserId());
            output = getAccountById(newAccountId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Can't get a reference to the source of data.",e);
        } catch(DataIntegrityViolationException e){
            throw new DaoException("Data breaks the rules.",e);
        }
        return output;
    }

    @Override
    public Account getAccountByUserName(String username) {
        Account output = null;
        String sql = "Select * from users\n" +
                "join accounts as ac on users.user_id = ac.user_id\n" +
                "Where username = ?;";

        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            if(results.next()){
                output = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Can't get a reference to the source of data.",e);
        } catch(DataIntegrityViolationException e){
            throw new DaoException("Data breaks the rules.",e);
        }
        return output;
    }

    @Override
    public Account updateBalance(Account account) {
        Account output = null;
        String sql = "update account" +
                "set balance = ? where account_id = ?;";
        try{
            int numberOfRows = jdbcTemplate.update(sql,account.getBalance(),
                    account.getAccountId());
            if (numberOfRows == 0){
                throw new DaoException("No accounts were updated");
            } else {
                output = getAccountById(account.getAccountId());
            }
        }catch (Exception e) {
            throw new DaoException("Cannot update account", e);
        }
        return output;
    }

    @Override
    public int deleteAccount(int id) {
        int numberOfRows = 0;
        String sql = "delete from accounts where account_id = ?;";
        try {
            numberOfRows = jdbcTemplate.update(sql, id);
        } catch (Exception e){
            throw new DaoException("Cannot delete account", e);
        }
        return numberOfRows;
    }
    private Account mapRowToAccount (SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;

    }
}

