package com.techelevator.tebucks.security.dao;

import com.techelevator.tebucks.security.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfers();

    Transfer getTransferByTransferId(int id);

    List<Transfer> getTransfersByAccountId(int account_id);

    List<Transfer> getTransfersByAccountTo(int account_id);

    List<Transfer> getTransferByAccountFrom(int account_id);

    List<Transfer> getTransferByStatusId(int status_id);

    Transfer updateTransferStatus(Transfer transfer);

    int deleteTransfer(int id);

    Transfer createTransfer(Transfer transfer);


}
