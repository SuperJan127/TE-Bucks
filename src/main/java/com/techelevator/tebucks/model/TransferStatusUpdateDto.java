package com.techelevator.tebucks.model;

public class TransferStatusUpdateDto {

    private String transferStatus;

    public TransferStatusUpdateDto(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public TransferStatusUpdateDto() {}

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }
}
