package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface AccountDao {

    public List<Account> getAllAccounts();

    public Account getAccountByAccountId(int accountId);

    public Account getAccountByUserId(int userId);

    public Account getCurrentUserAccount();

    public Transfer getTransfers(int accountId);

    public Transfer createTransfers(int accountId);









    /*


    getAccountBalance(accountId)
    getTransfers()
    getTransfersByAccount()
    createTransfer(transferType)
     */
}
