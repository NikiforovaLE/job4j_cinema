package ru.job4j.service;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.model.Account;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;

public class AccountService implements Service<Account> {
    private final BasicDataSource pool = new BasicDataSource();

    private AccountService() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(AccountService.class.getClassLoader()
                                .getResourceAsStream("db.properties"))
                )
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    @Override
    public Collection<Account> findAll() {
        return null;
    }

    @Override
    public Account findById(int id) {
        return null;
    }

    private static final class Lazy {
        private static final AccountService INST = new AccountService();
    }

    public static AccountService instOf() {
        return AccountService.Lazy.INST;
    }

    public boolean createAccount(String userName, String phone, String email) {
        boolean result = false;
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO accounts (username, phone, email) VALUES (?,?,?) ");
            ps.setString(1, userName);
            ps.setString(2, phone);
            ps.setString(3, email);
            result = ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
