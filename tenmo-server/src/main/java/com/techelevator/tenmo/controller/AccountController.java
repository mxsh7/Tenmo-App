package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private final AccountDao dao;

    public AccountController(AccountDao dao) {
        this.dao = dao;
    }

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public List<Account> listAccounts() {

        List<Account> accounts = dao.getAllAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            accounts.get(i).setBalance(BigDecimal.ZERO);

        }return accounts;
    }

    @RequestMapping(path = "/myaccount", method = RequestMethod.GET)
    public Account myAccount(Principal principal) {
        return dao.getCurrentUserAccount(principal);
    }

    @RequestMapping(path = "/myaccount/transfers", method = RequestMethod.GET)
    public List<Transfer> listTransfers(Principal principal) {
        Account userAccount = dao.getCurrentUserAccount(principal);
        return dao.getTransfers(userAccount);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/myaccount/transfers", method = RequestMethod.POST)
    public void createTransfer(@Valid @RequestBody Transfer transfer, Principal principal) {
        boolean createTransferSuccessful;
        Account userAccount = dao.getCurrentUserAccount(principal);

        if (transfer.getType() == 1) {
            transfer.setFromAccount(transfer.getToAccount());
            transfer.setToAccount(userAccount.getAccountId());
        } else {
            transfer.setFromAccount(userAccount.getAccountId());
            transfer.setToAccount(transfer.getToAccount());
        }

        createTransferSuccessful = dao.createTransfer(transfer);
        if(createTransferSuccessful == true && transfer.getType() == 2){
            int balanceResult = userAccount.getBalance().compareTo(transfer.getAmount());
            int amountResult = transfer.getAmount().compareTo(BigDecimal.ZERO);

            if (balanceResult == -1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance.");
            } else if (amountResult <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer amount cannot be negative.");
            } else {
                Account otherAccount = dao.getAccountByAccountId(transfer.getToAccount());
                userAccount.setBalance(userAccount.getBalance().subtract(transfer.getAmount()));
                boolean updateSuccessful = dao.updateAccount(userAccount);
                otherAccount.setBalance(otherAccount.getBalance().add(transfer.getAmount()));
                updateSuccessful = updateSuccessful && dao.updateAccount(otherAccount);
                if (!updateSuccessful) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while processing transfer.");
                }
            }
        }
    }

    @RequestMapping(path = "/myaccount/transfers", method = RequestMethod.PUT)
    public void updateTransfer(@Valid @RequestBody Transfer transfer, Principal principal) {
        Account userAccount = dao.getCurrentUserAccount(principal);
        Account transferReceiver = dao.getAccountByAccountId(transfer.getToAccount());

       boolean completeTransfer = dao.updateTransfer(transfer);
        if(completeTransfer == true && transfer.getStatus() == 2){
            userAccount.setBalance(userAccount.getBalance().subtract(transfer.getAmount()));

            boolean updateSuccessful = dao.updateAccount(userAccount);
            transferReceiver.setBalance(transferReceiver.getBalance().add(transfer.getAmount()));
            updateSuccessful = updateSuccessful && dao.updateAccount(transferReceiver);
            if (!updateSuccessful) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while processing transfer.");
            }
        }
    }


}
