package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    public static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public Account[] getAllAccounts() {
        Account[] accounts = null;
        try {
            ResponseEntity<Account[]> response =
                    restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(), Account[].class);
            accounts = response.getBody();

        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accounts;

    }

    public BigDecimal getBalance(){

        Account account = null;
        try{
            ResponseEntity<Account> response =
                     restTemplate.exchange(API_BASE_URL + "myaccount", HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException | NullPointerException e) {
            BasicLogger.log(e.getMessage());
        }
        return account.getBalance();
    }

    public Transfer[] getTransfers() {
        Transfer[] transfers = null;
        try{
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + "myaccount/transfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException | NullPointerException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

//    public BigDecimal sendBucks(BigDecimal amountToSend){
//
//        BigDecimal balance = getBalance();
//        int res =(balance.compareTo(amountToSend));
//        String str1 = "Both values are equal ";
//        String str2 = "Balance Value is greater ";
//        String str3 = "Amount to send value is greater";
//
//        if( res == 0 || res == 1  ) {
//
//            System.out.println("transfer is successful");
//        }
//        else {
//            System.out.println( "Insufficient balance" );
//        }
//
//
//    }



        // while balance > 0
        // senders amount to send = 0
        // then sender balance = balance - amount
        //receivers balance = balance + amount

//
//    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }


}