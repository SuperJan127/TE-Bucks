package com.techelevator.tebucks.model;

import com.techelevator.tebucks.security.model.User;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private String transferType = "";
    private String transferStatus = "";
    private User userFrom;
    private User userTo;
    BigDecimal amount = new BigDecimal("0");

    public Transfer(int transferId, String transferType, String transferStatus, User userFrom,
                    User accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.userFrom = userFrom;
        this.userTo = accountTo;
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

    public void setTransferType(String transferType) {
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

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}





