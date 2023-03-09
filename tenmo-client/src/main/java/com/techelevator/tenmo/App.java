package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
             consoleService.printErrorMessage();
        }accountService.setAuthToken(currentUser.getToken());
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        System.out.println("======================================");
        System.out.println("Your balance is: $"+accountService.getBalance());
	}

	private void viewTransferHistory() {
        Transfer[] transfers = accountService.getTransfers();
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID      From/To      Amount");
        for (int i = 0; i < transfers.length; i++) {
            Transfer transfer = transfers[i];
            String type = "";
            String otherAccountUsername = "";
            if (transfer.getFromUsername().equals(currentUser.getUser().getUsername())) {
                type = "To:  ";
                otherAccountUsername = transfer.getToUsername();
            } else {
                type = "From:";
                otherAccountUsername = transfer.getFromUsername();
            }
            System.out.println(transfer.getTransferId() + "    " + type + " " + otherAccountUsername + "   $" + transfer.getAmount());
        }
        System.out.println("-------------------------------------------");


	}

    private void viewPendingRequests() {
        Transfer[] transfers = accountService.getTransfers();
        System.out.println("-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID      To      Amount");
        for (int i = 0; i < transfers.length; i++) {
            Transfer transfer = transfers[i];
            if(transfer.getType() == 1 && transfer.getStatus() == 1 && transfer.getFromUsername().equals(currentUser.getUser().getUsername())) {
                String type = "";
                String otherAccountUsername = "";

                    type = "To:  ";
                    otherAccountUsername = transfer.getToUsername();
                System.out.println(transfer.getTransferId() + "    " + type + " " + otherAccountUsername + "   $" + transfer.getAmount());
            }
        }
        System.out.println("-------------------------------------------");
		
	}

	private void sendBucks() {
        Account[] accounts = accountService.getAllAccounts();
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID      Name");
        for (int i = 0; i < accounts.length; i++) {
            Account account = accounts[i];
            System.out.println(account.getUserId());
        }
        System.out.println("-------------------------------------------");
        // TODO Include User Name In Account Object
        int userId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel)");
        int accountId = 0;
        for (int i = 0; i < accounts.length; i++) {
            Account account = accounts[i];
            if (account.getUserId() == userId) {
                accountId = account.getAccountId();
            }
        }
        BigDecimal amountToSend = consoleService.promptForBigDecimal("Enter Amount");
        accountService.sendBucks(currentUser.getUser().getUsername(), accountId, amountToSend);
	}

    /*
    -------------------------------------------
    Users
    ID Name
    -------------------------------------------
    313 Bernice
    54 Larry
    ---------

     */

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
