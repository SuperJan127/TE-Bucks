package com.techelevator.tebucks.security.model;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private String transferType;
    private String transferStatus;
    private User accountFrom;
    private User accountTo;
    BigDecimal amount = new BigDecimal("0");

    public Transfer(int transferId, String transferType, String transferStatus, User accountFrom,
                    User accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Transfer() {
    }

    public int getTransferId() {
        return transferId;
    }


    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferTypeI) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public User getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(User accountFrom) {
        this.accountFrom = accountFrom;
    }

    public User getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(User accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}





