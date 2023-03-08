package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface AccountDao {

    public List<Account> getAllAccounts();

    public Account getAccountByAccountId(int accountId);

    public Account getAccountByUserId(int userId);

    public Account getCurrentUserAccount(Principal principal);

    public List<Transfer> getTransfers(int accountId);

    public Transfer createTransfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount);

}
