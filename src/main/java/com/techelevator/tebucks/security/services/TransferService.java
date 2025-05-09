package com.techelevator.tebucks.security.services;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.security.dao.AccountDao;
import com.techelevator.tebucks.security.dao.TransferDao;
import com.techelevator.tebucks.security.dao.UserDao;
import com.techelevator.tebucks.security.model.Account;
import com.techelevator.tebucks.security.model.Transfer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferService {

    private AccountService accountService;

    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;

    private final int TRANSFER_TYPE_SEND = 2;

    private final int TRANSFER_TYPE_REQUEST = 1;

    private final int STATUS_PENDING = 1;

    private final int STATUS_APPROVED = 2;

    private final int STATUS_REJECTED = 3;

    public TransferService(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    public Transfer sendTransfer(int userFrom, int userTo, BigDecimal amount) {
        Account sender = accountDao.getAccountByUserId(userFrom);
        Account receiver = accountDao.getAccountByUserId(userTo);
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
        transfer.setAccountFrom(sender.getAccountId());
        transfer.setAccountTo(receiver.getAccountId());
        transfer.setTransferTypeId(TRANSFER_TYPE_SEND);
        transfer.setTransferStatusId(STATUS_APPROVED);
        transfer.setAmount(amount);

        Transfer createdTransfer = transferDao.createTransfer(transfer);

        accountService.updateBalance(sender, amount, false);
        accountService.updateBalance(receiver, amount, true);

        return createdTransfer;
    }

    public Transfer requestTransfer(int userFrom, int userTo, BigDecimal amount) {
        Account sender = accountDao.getAccountByUserId(userFrom);
        Account receiver = accountDao.getAccountByUserId(userTo);
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        if (userFrom == userTo) {
            throw new IllegalArgumentException("Cannot request money from the same account");
        }
        Transfer request = new Transfer();
        request.setAccountFrom(sender.getAccountId());
        request.setAccountTo(receiver.getAccountId());
        request.setTransferTypeId(TRANSFER_TYPE_REQUEST);
        request.setTransferStatusId(STATUS_PENDING);
        request.setAmount(amount);

        Transfer createdRequest = transferDao.createTransfer(request);

        return createdRequest;
    }
    
    public Transfer approveTransfer(int transferId){
        Transfer newTransfer = transferDao.getTransferByTransferId(transferId);
        Account accountTo = accountDao.getAccountById(newTransfer.getAccountTo());
        Account accountFrom = accountDao.getAccountByUserId(newTransfer.getAccountFrom());
        newTransfer.setTransferStatusId(STATUS_APPROVED);
        accountService.updateBalance(accountTo, newTransfer.getAmount(), true);
        accountService.updateBalance(accountFrom, newTransfer.getAmount(), false);

        return newTransfer;
    }

    public Transfer rejectTransfer(int transferId){
        Transfer newTransfer = transferDao.getTransferByTransferId(transferId);
        newTransfer.setTransferStatusId(STATUS_REJECTED);
        return newTransfer;
    }
    
    
}
