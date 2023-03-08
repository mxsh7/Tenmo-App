package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public JdbcAccountDao ( JdbcTemplate jdbcTemplate, UserDao userDao ) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }


    @Override
    public List<Account> getAllAccounts() {

        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance\n" +
                "\tFROM public.account;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Account account = mapAccount(results);
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
            account = mapAccount(results);

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
            account = mapAccount(results);

        }

        return account;
    }

    @Override
    public Account getCurrentUserAccount(Principal principal) {
        Account account = null;
        User userId = userDao.findByUsername(principal.getName());
        String sql = "SELECT account_id, user_id, balance\n" +
                "\tFROM public.account\n" +
                "\tWHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId.getId());
        if (results.next()) {
            account = mapAccount(results);
        }

        return account;
    }

    @Override
    public List<Transfer> getTransfers(Account account) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount\n" +
                "\tFROM public.transfer\n" +
                "\tWHERE account_from = ? OR account_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account.getAccountId(), account.getAccountId());
        while(results.next()) {
            Transfer transfer = mapTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }


    @Override
    public Transfer createTransfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount ) {

        String sql = "INSERT INTO public.transfer(\n" +
                "\ttransfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "\tVALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeId, transferStatusId, accountFrom, accountTo, amount);
        Transfer transfer = new Transfer();
        transfer.setTransferId(newTransferId);
        transfer.setStatus(transferStatusId);
        transfer.setType(transferStatusId);
        transfer.setFromAccount(accountFrom);
        transfer.setToAccount(accountTo);
        transfer.setAmount(amount);
        return transfer;
    }

    private Account mapAccount(SqlRowSet results){
        Account account = new Account();
        account.setAccountId(results.getInt("account_id"));
        account.setUserId(results.getInt("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }

    private Transfer mapTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setStatus(results.getInt("transfer_status_id"));
        transfer.setType(results.getInt("transfer_type_id"));
        transfer.setFromAccount(results.getInt("account_from"));
        transfer.setToAccount(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}
