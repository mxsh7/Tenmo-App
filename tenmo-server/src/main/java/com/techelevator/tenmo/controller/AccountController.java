package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private AccountDao dao;

    public AccountController(AccountDao dao) {
        this.dao = dao;
    }

@RequestMapping(path = "", method = RequestMethod.GET)
    public List<Account> listAccounts() {
    return dao.getAllAccounts();
}

@RequestMapping(path = "/myaccount")
    public Account myAccount(Principal principal){

        return dao.getCurrentUserAccount(principal);
}

    @RequestMapping(path = "/myaccount/transfers")
    Public List<Account>
}
