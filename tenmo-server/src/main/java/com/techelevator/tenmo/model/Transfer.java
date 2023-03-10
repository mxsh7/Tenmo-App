package com.techelevator.tenmo.model;

import javax.validation.constraints.Positive;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    @Positive
    private BigDecimal amount;
    private int type;
    private int status;
    private int fromAccount;
    private int toAccount;
    private String fromUsername;
    private String toUsername;

    final static int REQUEST = 1;
    final static int SEND = 2;

    final static int PENDING = 1;
    final static int APPROVED = 2;
    final static int REJECTED = 3;

    public Transfer(int transferId, BigDecimal amount, int type, int status, int fromAccount, int toAccount, String fromUsername, String toUsername) {
        this.transferId = transferId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
    }

    public Transfer() {

    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public void setToAccount(int toAccount) {
        this.toAccount = toAccount;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }
}
