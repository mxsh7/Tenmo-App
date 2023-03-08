package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao ( JdbcTemplate jdbcTemplate ) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Account> getAllAccounts() {

        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance\n" +
                "\tFROM public.account;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Account account = mapAccounts(results);
            accounts.add(account);
        }

        return accounts;
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance\n" +
                "\tFROM public.account\n" +
                "\tWHERE account_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            account = mapAccounts(results);

        }

        return account;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance\n" +
                "\tFROM public.account\n" +
                "\tWHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            account = mapAccounts(results);

        }

        return account;
    }

    @Override
    public Account getCurrentUserAccount() {
        return null;
    }

    @Override
    public Transfer getTransfers(int accountId) {
        return null;
    }

    @Override
    public Transfer createTransfers(int accountId) {
        return null;
    }

    private Account mapAccounts(SqlRowSet results){
        Account account = new Account();
        account.setAccountId(results.getInt("account_id"));
        account.setOwner(results.getInt("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }
}
