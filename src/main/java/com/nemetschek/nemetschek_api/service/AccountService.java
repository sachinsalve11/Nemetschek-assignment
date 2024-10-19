package com.nemetschek.nemetschek_api.service;

import com.nemetschek.nemetschek_api.model.Account;
import com.nemetschek.nemetschek_api.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

   /* public AccountService(AccountRepository accountRepository) {

        this.accountRepository = accountRepository;

    }*/

    public Account createAccount(String name, BigDecimal initialBalance) {
        Account account = new Account();
        account.setName(name);
        account.setBalance(initialBalance);
        return accountRepository.save(account);

    }

    public Account getAccount(Long accountId) {

        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional
    public void updateAccount(Long accountId, Account updatedAccount) {

        Account existingAccount = getAccount(accountId);
        existingAccount.setName(updatedAccount.getName());
        accountRepository.save(existingAccount);

    }

    @Transactional
    public void suspendAccount(Long accountId) {

        Account account = getAccount(accountId);
        account.setActive(false);

        accountRepository.save(account);
    }
}
