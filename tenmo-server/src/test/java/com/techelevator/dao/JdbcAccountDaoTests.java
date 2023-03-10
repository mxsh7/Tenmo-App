package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests{

    protected static final Account ACCOUNT_1 = new Account(01, 1001, new BigDecimal("500.00"), "user1");
    protected static final Account ACCOUNT_2 = new Account(02, 1002, new BigDecimal("600.00"), "user2");
    protected static final Account ACCOUNT_3 = new Account(03, 1003, new BigDecimal("700.00"), "user3");

    protected static  final Transfer TRANSFER_1 = new Transfer(01, new BigDecimal("100.00"), 2, 2, 01,02,"user1", "user2");
    protected static  final Transfer TRANSFER_2 = new Transfer(02, new BigDecimal("200.00"), 1, 3, 03,02,"user3", "user2");

    private JdbcAccountDao dao;
    private Account testAccount;
    private Transfer testTransfer;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcAccountDao(jdbcTemplate, new JdbcUserDao(jdbcTemplate));

        testTransfer = new Transfer(0, new BigDecimal("200"), 1, 3, 03,02,"user3", "user2");
    }


    @Test
    public void findAll_Accounts(){
        List<Account> account = dao.getAllAccounts();

        Assert.assertNotNull(account);
        Assert.assertEquals(3,account.size());
        assertAccountsMatch(ACCOUNT_1, account.get(0));
        assertAccountsMatch(ACCOUNT_2, account.get(1));
        assertAccountsMatch(ACCOUNT_3, account.get(2));
    }

    @Test
    public void getAccountById_given_invalid_account_returnsNUll(){
        Account account = dao.getAccountByAccountId(-1);
        Assert.assertNull(account);

    }
    @Test
    public void findByAccount_given_valid_user_returns_account() {
        Account actualAccount = dao.getAccountByAccountId(ACCOUNT_1.getAccountId());

        assertAccountsMatch(ACCOUNT_1, actualAccount);
    }

    @Test
    public void findByAccount_given_userID_returns_account() {
        Account userAccount = dao.getAccountByUserId(ACCOUNT_1.getUserId());

        assertAccountsMatch(ACCOUNT_1, userAccount);
    }

    @Test
    public void findAll_Transfers(){
        List<Transfer> transfers = dao.getTransfers(ACCOUNT_1);

        Assert.assertNotNull(transfers);
        assertTransfersMatch(TRANSFER_1,transfers.get(0));
    }
    @Test
    public void created_Transfer_has_expected_values(){
        boolean createdTransfer = dao.createTransfer(testTransfer);

        Assert.assertTrue(createdTransfer);

    }
    @Test
    public void update_transfer_has_expected_values(){
        Transfer transferUpdate = dao.getTransferById(01);

        transferUpdate.setFromAccount(02);
        transferUpdate.setToAccount(01);
        transferUpdate.setType(1);
        transferUpdate.setAmount(new BigDecimal("150.00"));
        transferUpdate.setFromUsername("user2");
        transferUpdate.setToUsername("user1");
        transferUpdate.setStatus(2);

        dao.updateTransfer(transferUpdate);

        Transfer retrieveTransfer = dao.getTransferById(01);
        assertTransfersMatch(transferUpdate,retrieveTransfer);
    }


    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
        Assert.assertEquals(expected.getToAccount(), actual.getToAccount());
        Assert.assertEquals(expected.getType(), actual.getType());
        Assert.assertEquals(expected.getFromAccount(), actual.getFromAccount());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }

    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }

}
