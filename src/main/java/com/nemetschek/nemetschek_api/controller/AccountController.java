package com.nemetschek.nemetschek_api.controller;

import com.nemetschek.nemetschek_api.model.Account;
import com.nemetschek.nemetschek_api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /*public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }*/

    @PostMapping
    public Account createAccount(@RequestParam String name, @RequestParam BigDecimal initialBalance) {

        return accountService.createAccount(name, initialBalance);
    }

    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable Long accountId) {

        return accountService.getAccount(accountId);
    }

    @PutMapping("/{accountId}")
    public void updateAccount(@PathVariable Long accountId, @RequestBody Account account) {

        accountService.updateAccount(accountId, account);
    }

    @DeleteMapping("/{accountId}")
    public void suspendAccount(@PathVariable Long accountId) {


        accountService.suspendAccount(accountId);
    }
}
