package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

    public List<Account> getAllAccounts();

    public Account getAccountByAccountId(int accountId);

    public Account getAccountByUserId(int userId);

    public Account getCurrentUserAccount(int userId);


    /*
    getAllAccounts()
    getAccountByUserId()
    getAccountById()
    getPrincepalAccount()
    getAccountBalance(accountId)
    getTransfers()
    getTransfersByAccount()
    createTransfer(transferType)
     */
}
