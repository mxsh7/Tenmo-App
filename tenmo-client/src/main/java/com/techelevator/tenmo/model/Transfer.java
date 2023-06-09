package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    final static int REQUEST = 1;
    final static int SEND = 2;

    final static int PENDING = 1;
    final static int APPROVED = 2;
    final static int REJECTED = 3;

    private int transferId;
    private BigDecimal amount;
    private int type;
    private int status;
    private int fromAccount;
    private int toAccount;
    private String fromUsername;
    private String toUsername;

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

    public String getTypeDesc() {
        if (this.getType() == 1) {
            return "Request";
        } else {
            return "Send";
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        if (this.getStatus() == 1) {
            return "Pending";
        } else if (this.getStatus() == 2) {
            return "Approved";
        } else {
            return "Rejected";
        }
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
