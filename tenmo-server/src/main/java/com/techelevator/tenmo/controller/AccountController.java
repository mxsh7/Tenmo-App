package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        return dao.getAllAccounts();
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
            boolean completeTransfer = false;
        Account userAccount = dao.getCurrentUserAccount(principal);

        if (transfer.getType() == 1) {
            transfer.setFromAccount(transfer.getToAccount());
            transfer.setToAccount(userAccount.getAccountId());
        } else {
            transfer.setFromAccount(userAccount.getAccountId());
            transfer.setToAccount(transfer.getToAccount());
        }

        // TODO Validate transfer
        // TODO Check for insufficient balance
        completeTransfer = dao.createTransfer(transfer);
        if(completeTransfer == true && transfer.getType() == 2){
            Account otherAccount = dao.getAccountByAccountId(transfer.getToAccount());
            userAccount.setBalance(userAccount.getBalance().subtract(transfer.getAmount()));
            // TODO Check That Update Worked
            dao.updateAccount(userAccount);
            otherAccount.setBalance(otherAccount.getBalance().add(transfer.getAmount()));
            dao.updateAccount(otherAccount);
        }

    }

    @RequestMapping(path = "/myaccount/transfers", method = RequestMethod.PUT)
    public void updateTransfer(@Valid @RequestBody Transfer transfer, Principal principal) {
        Account userAccount = dao.getCurrentUserAccount(principal);
        Account transferReceiver = dao.getAccountByAccountId(transfer.getToAccount());

       boolean completeTransfer = dao.updateTransfer(transfer);
        if(completeTransfer == true && transfer.getStatus() == 2){
            userAccount.setBalance(userAccount.getBalance().subtract(transfer.getAmount()));
            // TODO Check That Update Worked
            dao.updateAccount(userAccount);
            transferReceiver.setBalance(transferReceiver.getBalance().add(transfer.getAmount()));
            dao.updateAccount(transferReceiver);
        }
    }


}
