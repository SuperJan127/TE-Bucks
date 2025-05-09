package com.techelevator.tebucks.security.services;

import com.techelevator.tebucks.security.dao.AccountDao;
import com.techelevator.tebucks.security.dao.TransferDao;
import com.techelevator.tebucks.security.dao.UserDao;
import com.techelevator.tebucks.security.model.Account;
import com.techelevator.tebucks.security.model.Transfer;
import com.techelevator.tebucks.security.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferService {

    private AccountService accountService;

    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;


    public TransferService(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    public Transfer sendTransfer(User userFrom, User userTo, BigDecimal amount) {
           Account sender = accountDao.getAccountByUserId(userFrom.getId());
           Account receiver = accountDao.getAccountByUserId(userTo.getId());
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        if (userFrom == userTo) {
            throw new IllegalArgumentException("Cannot transfer money to the same account");
        }
        if (sender.getBalance().compareTo(amount) < 0 ) {
            throw new IllegalArgumentException("Insufficient Funds") ;
        }

        Transfer transfer = new Transfer();
        transfer.setUserFrom(userFrom);
        transfer.setUserTo(userTo);
        transfer.setTransferType("Send");
        transfer.setTransferStatus("Approved");
        transfer.setAmount(amount);

        Transfer createdTransfer = transferDao.createTransfer(transfer);

        accountService.updateBalance(sender, amount, false);
        accountService.updateBalance(receiver, amount, true);

        return createdTransfer;
    }

    public Transfer requestTransfer(User userFrom, User userTo, BigDecimal amount) {
        Account sender = accountDao.getAccountByUserId(userFrom.getId());
        Account receiver = accountDao.getAccountByUserId(userTo.getId());
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        if (userFrom == userTo) {
            throw new IllegalArgumentException("Cannot request money from the same account");
        }
        Transfer request = new Transfer();
        request.setUserFrom(userFrom);
        request.setUserTo(userTo);
        request.setTransferType("Request");
        request.setTransferStatus("Pending");
        request.setAmount(amount);

        Transfer createdRequest = transferDao.createTransfer(request);

        return createdRequest;
    }
    
    public Transfer approveTransfer(int transferId){
        Transfer newTransfer = transferDao.getTransferByTransferId(transferId);
        User userTo = newTransfer.getUserTo();
        User userFrom = newTransfer.getUserFrom();
        newTransfer.setTransferStatus("Approved");
        accountService.updateBalance(accountDao.getAccountByUserId(userTo.getId()),
                newTransfer.getAmount(),
                true);
        accountService.updateBalance(accountDao.getAccountByUserId(userFrom.getId()),
                newTransfer.getAmount(), false);

        return newTransfer;
    }

    public Transfer rejectTransfer(int transferId){
        Transfer newTransfer = transferDao.getTransferByTransferId(transferId);
        newTransfer.setTransferStatus("Rejected");
        return newTransfer;
    }
    
    
}
