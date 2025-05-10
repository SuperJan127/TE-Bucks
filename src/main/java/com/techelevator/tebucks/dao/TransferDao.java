package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfers();

    Transfer getTransferByTransferId(int id);

    List<Transfer> getTransfersByAccountId(int account_id);

    List<Transfer> getTransferByStatusId(int status_id);

    Transfer updateTransferStatus(Transfer transfer);

    int deleteTransfer(int id);

    Transfer createTransfer(Transfer transfer);


}
