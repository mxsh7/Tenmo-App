package com.techelevator.tenmo.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public class Account {
    @NotEmpty
    private int accountId;
    @NotEmpty
    private int userId;
    @PositiveOrZero
    private BigDecimal balance;
    private String username;

    public Account(int accountId, int userId, BigDecimal balance, String username) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.username = username;
    }

    public Account() {

    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
