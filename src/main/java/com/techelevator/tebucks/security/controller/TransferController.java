package com.techelevator.tebucks.security.controller;

import com.techelevator.tebucks.security.dao.AccountDao;
import com.techelevator.tebucks.security.dao.TransferDao;
import com.techelevator.tebucks.security.dao.UserDao;
import com.techelevator.tebucks.security.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

//@PreAuthorize("isAuthenticated()")
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
            return transfer;
        }
    }
    //TODO why are all transfers requests
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path="/transfers")
    public Transfer createTransfer(@RequestBody NewTransferDto newTransferDto){
        Transfer transfer = new Transfer();
        transfer.setTransferType(newTransferDto.getTransferType());
        if(transfer.getTransferType().equalsIgnoreCase("Send")){
            transfer.setTransferStatus("Approved");
        } else {
            transfer.setTransferStatus("Pending");
        }
        transfer.setUserFrom(userDao.getUserById(newTransferDto.getUserFrom()));
        transfer.setUserTo(userDao.getUserById(newTransferDto.getUserTo()));
        transfer.setAmount(newTransferDto.getAmount());
        return transferDao.createTransfer(transfer);
    }

    @PutMapping(path = "/transfers/{id}/status")
    public Transfer updateTransferStatus(@RequestBody TransferStatusUpdateDto transferStatusUpdateDto, @PathVariable int id){
        Transfer transfer = transferDao.getTransferByTransferId(id);
        transfer.setTransferStatus(transferStatusUpdateDto.getTransferStatus());
        return transfer;
    }


    @GetMapping(path = "/users")
    public List<User> getUsers(Principal principal){
        String username = principal.getName();
        return userDao.getUsers(username);
    }



}
