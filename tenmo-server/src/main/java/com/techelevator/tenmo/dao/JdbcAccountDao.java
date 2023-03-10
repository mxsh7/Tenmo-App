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
            account.setUsername(getAccountUsername(account.getAccountId()));
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
            account.setUsername(getAccountUsername(accountId));
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
            account.setUsername(getAccountUsername(account.getAccountId()));
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
            account.setUsername(getAccountUsername(account.getAccountId()));
        }

        return account;
    }

    @Override
    public String getAccountUsername(int accountId) {
        Account account = null;
        String sql = "SELECT username\n" +
                "FROM account\n" +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id\n" +
                "WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            return results.getString("username");
        }
        return "";
    }

    @Override
    public List<Transfer> getTransfers(Account account) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to,\n" +
                "amount, from_user.username AS from_username, to_user.username AS to_username\n" +
                "FROM public.transfer\n" +
                "LEFT JOIN account AS from_account ON transfer.account_from = from_account.account_id\n" +
                "JOIN tenmo_user AS from_user ON from_account.user_id = from_user.user_id\n" +
                "LEFT JOIN account AS to_account ON transfer.account_to = to_account.account_id\n" +
                "JOIN tenmo_user AS to_user ON to_account.user_id = to_user.user_id\n" +
                "WHERE  account_from= ? OR account_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account.getAccountId(), account.getAccountId());
        while(results.next()) {
            Transfer transfer = mapTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }


    @Override
    public boolean createTransfer(Transfer transfer) {
        boolean success = false;
        try {
            String sql = "INSERT INTO public.transfer(\n" +
                    "\ttransfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                    "\tVALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
             jdbcTemplate.queryForObject(sql, Integer.class, transfer.getType(), transfer.getStatus(),
                    transfer.getFromAccount(), transfer.getToAccount(), transfer.getAmount());
             success = true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return success;

    }


    @Override
    public boolean updateTransfer(Transfer transfer) {
        boolean success = false;
        try {
            String sql = "UPDATE public.transfer\n" +
                    "\tSET transfer_id=?, transfer_type_id=?, transfer_status_id=?, account_from=?, account_to=?, amount=?\n" +
                    "\tWHERE transfer_id=?;";
            jdbcTemplate.update(sql, transfer.getTransferId(), transfer.getType(), transfer.getStatus(), transfer.getFromAccount(),
                    transfer.getToAccount(), transfer.getAmount(), transfer.getTransferId());
            success = true;
        } catch (Exception e) {

        }
        return success;
    }


    @Override
    public boolean updateAccount(Account account){
        boolean update = false;
        try{
            String sql = "UPDATE public.account\n" +
                    "\tSET account_id=?, user_id=?, balance=?\n" +
                    "\tWHERE account_id = ?;";
            jdbcTemplate.update(sql, account.getAccountId(), account.getUserId(),account.getBalance(), account.getAccountId());
        update = true;
        }catch (Exception e){

        }
        return update;
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
        transfer.setFromUsername(results.getString("from_username"));
        transfer.setToUsername(results.getString("to_username"));
        return transfer;
    }
}
