package ru.job4j.service;

import ru.job4j.model.Account;
import ru.job4j.persistence.DBAccounts;

import java.util.Collection;

public class AccountService implements Service<Account> {
    private final DBAccounts accountsFromDB = DBAccounts.instOf();

    private static final class Lazy {
        private static final AccountService INST = new AccountService();
    }

    public static AccountService instOf() {
        return AccountService.Lazy.INST;
    }

    @Override
    public Collection<Account> findAll() {
        return accountsFromDB.findAll();
    }

    @Override
    public Account findById(int id) {
        return accountsFromDB.findById(id);
    }

    public Account saveAccount(Account account) {
        return accountsFromDB.save(account);
    }
}
