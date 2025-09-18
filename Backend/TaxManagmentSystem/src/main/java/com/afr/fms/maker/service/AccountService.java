package com.afr.fms.maker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.maker.entity.Account;
import com.afr.fms.maker.mapper.AccountMapper;


@Service
public class AccountService {
    @Autowired
    private AccountMapper AccountMapper;

    public List<Account> getAccounts() {
        return AccountMapper.getAccounts();
    }

    public List<Account> getActiveAccounts() {
        return AccountMapper.getActiveAccounts();
    }

    public void createAccount(Account account) {
        AccountMapper.createAccount(account);

    }

    public void updateAccount(Account account) {
        AccountMapper.updateAccount(account);
    }

    public void deleteAccount(Account account) {
        AccountMapper.deleteAccount(account);
    }

      public void activateAccount(Account account) {
        AccountMapper.activateAccount(account);
    }

    public List<Account> searchAccount(String searchKey) {
        return AccountMapper.searchAccount(searchKey);
    }

    public Account getAccountById(Long id) {
        return AccountMapper.getAccountById(id);
    }
}
