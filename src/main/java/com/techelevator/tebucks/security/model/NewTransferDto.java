package com.techelevator.tebucks.security.model;

public class NewTransferDto {

    private int userFrom;

    private int userTo;

    private double amount;

    private String transferType;


    public NewTransferDto(int userFrom, int userTo, double amount, String transferType) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.amount = amount;
        this.transferType = transferType;
    }

    public int getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(int userFrom) {
        this.userFrom = userFrom;
    }

    public int getUserTo() {
        return userTo;
    }

    public void setUserTo(int userTo) {
        this.userTo = userTo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
}
