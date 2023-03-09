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
        Account transferReceiver = dao.getAccountByAccountId(transfer.getToAccount());

        transfer.setStatus(2);
        transfer.setFromAccount(userAccount.getAccountId());

        // TODO Validate transfer
        completeTransfer = dao.createTransfer(transfer);
        if(completeTransfer == true){
            userAccount.setBalance(userAccount.getBalance().subtract(transfer.getAmount()));
            dao.updateAccount(userAccount);
            transferReceiver.setBalance(transferReceiver.getBalance().add(transfer.getAmount()));
            dao.updateAccount(transferReceiver);

        }

        // TODO Update Balances

    }


}
