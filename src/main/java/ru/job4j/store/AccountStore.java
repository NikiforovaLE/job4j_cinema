package ru.job4j.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccountStore implements Store<Account> {
    private final BasicDataSource pool = new BasicDataSource();

    @Override
    public Collection<Account> findAll(int id) {
        List<Account> accounts = new ArrayList<>();
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement("SELECT * FROM accounts;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(new Account(rs.getInt("id"), rs.getString("name"),
                            rs.getString("email"), rs.getString("phone")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account findById(int id) {
        return null;
    }

    @Override
    public void save(Account account) {

    }

    @Override
    public void delete(Account account) {

    }
}
