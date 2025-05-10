package com.techelevator.tebucks.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class NewTransferDto {

    private int userFrom;

    private int userTo;

    @DecimalMin(value = "0.01")
    private BigDecimal amount = new BigDecimal("0");

    private String transferType = "";


    public NewTransferDto(int userFrom, int userTo, BigDecimal amount, String transferType) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.amount = amount;
        this.transferType = transferType;
    }

    public NewTransferDto() {
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
}
