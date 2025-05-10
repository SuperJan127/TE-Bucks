package com.techelevator.tebucks.controllers;

import com.techelevator.tebucks.dao.AccountDao;
import com.techelevator.tebucks.dao.TransferDao;
import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/account")
@RestController
public class AccountController {

    private AccountDao accountDao;

    private TransferDao transferDao;

    public AccountController(AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }


    @GetMapping(path = "/balance")
    public Account getBalance(Principal principal){
        String username = principal.getName();
        return  accountDao.getAccountByUserName(username);
    }

    @GetMapping(path = "/transfers")
    public List<Transfer> getTransfers(Principal principal){
        String username = principal.getName();
        Account account = accountDao.getAccountByUserName(username);
        List<Transfer> transfers = transferDao.getTransfersByAccountId(account.getAccountId());
        return transfers;
    }

}
