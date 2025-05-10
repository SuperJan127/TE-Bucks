package com.techelevator.tebucks.controllers;

import com.techelevator.tebucks.dao.AccountDao;
import com.techelevator.tebucks.dao.TransferDao;
import com.techelevator.tebucks.model.NewTransferDto;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferStatusUpdateDto;
import com.techelevator.tebucks.security.dao.UserDao;
import com.techelevator.tebucks.security.model.*;
import com.techelevator.tebucks.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/api")
@RestController
public class TransferController {

    private AccountDao accountDao;

    private AccountService accountService;

    private TransferDao transferDao;

    private UserDao userDao;


    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao, AccountService accountService) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountService = accountService;
    }


    @GetMapping(path= "/transfers/{id}")
    public Transfer getTransfer(@PathVariable int id){
        Transfer transfer = transferDao.getTransferByTransferId(id);
        if(transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found");
        } else {
            return transfer;
        }
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path="/transfers")
    public Transfer createTransfer(@Valid @RequestBody NewTransferDto newTransferDto){
        Transfer transfer = new Transfer();
        if (newTransferDto.getAmount() == null || newTransferDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException( "Amount must be greater than zero");
        }

        transfer.setTransferType(newTransferDto.getTransferType());
        if(transfer.getTransferType().equalsIgnoreCase("Send")){

            transfer.setTransferStatus("Approved");
        } else {
            transfer.setTransferStatus("Pending");
        }
        transfer.setUserFrom(userDao.getUserById(newTransferDto.getUserFrom()));
        transfer.setUserTo(userDao.getUserById(newTransferDto.getUserTo()));
        transfer.setAmount(newTransferDto.getAmount());
        if (transfer.getTransferStatus().equalsIgnoreCase("Approved")) {
            accountService.updateBalance(accountDao.getAccountByUserName(transfer.getUserFrom().getUsername()), transfer.getAmount(), false);
            accountService.updateBalance(accountDao.getAccountByUserName(transfer.getUserTo().getUsername()), transfer.getAmount(), true);
        }
        return transferDao.createTransfer(transfer);
    }


    @Transactional
    @PutMapping(path = "/transfers/{id}/status")
    public Transfer updateTransferStatus(@Valid @RequestBody TransferStatusUpdateDto transferStatusUpdateDto, @PathVariable int id){
        Transfer transfer = transferDao.getTransferByTransferId(id);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "transfer not found");
        }
        if (transferStatusUpdateDto.getTransferStatus().equalsIgnoreCase("Approved")) {
            accountService.updateBalance(accountDao.getAccountByUserName(transfer.getUserFrom().getUsername()), transfer.getAmount(), false);
            accountService.updateBalance(accountDao.getAccountByUserName(transfer.getUserTo().getUsername()), transfer.getAmount(), true);
        }
        transfer.setTransferStatus(transferStatusUpdateDto.getTransferStatus());
        return transferDao.updateTransferStatus(transfer);
    }


    @GetMapping(path = "/users")
    public List<User> getUsers(Principal principal){
        String username = principal.getName();
        return userDao.getUsers(username);
    }



}
