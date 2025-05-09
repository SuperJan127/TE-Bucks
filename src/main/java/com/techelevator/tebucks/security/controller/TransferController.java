package com.techelevator.tebucks.security.controller;

import com.techelevator.tebucks.security.dao.AccountDao;
import com.techelevator.tebucks.security.dao.TransferDao;
import com.techelevator.tebucks.security.dao.UserDao;
import com.techelevator.tebucks.security.model.Account;
import com.techelevator.tebucks.security.model.Transfer;
import com.techelevator.tebucks.security.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/api")
@RestController
public class TransferController {

    private AccountDao accountDao;

    private TransferDao transferDao;

    private UserDao userDao;


    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @GetMapping(path= "/transfers/{id}")
    public Transfer getTransfer(@PathVariable int id){
        Transfer transfer = transferDao.getTransferByTransferId(id);
        if(transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found");
        } else {
            return null;
        }
    }


    @GetMapping(path = "/users")
    public List<User> getUsers(Principal principal){
        String username = principal.getName();
        return userDao.getUsers(username);
    }

}
