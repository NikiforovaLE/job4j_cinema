package ru.job4j.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.model.Account;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class DBAccounts implements Repository<Account> {
    private final BasicDataSource pool = new BasicDataSource();

    private DBAccounts() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(DBAccounts.class.getClassLoader()
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

    private static final class Lazy {
        private static final DBAccounts INST = new DBAccounts();
    }

    public static DBAccounts instOf() {
        return DBAccounts.Lazy.INST;
    }

    @Override
    public Collection<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement("SELECT * FROM accounts;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(new Account(rs.getInt("id"), rs.getString("username"),
                            rs.getString("phone"), rs.getString("email")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account findById(int id) {
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM accounts WHERE id = ?;");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    new Account(rs.getInt("id"), rs.getString("username"),
                            rs.getString("phone"), rs.getString("email"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account save(Account account) {
        if (account.getId() == 0) {
            create(account);
        } else {
            update(account);
        }
        return account;
    }

    private void create(Account account) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO accounts(username, phone, email) VALUES (?,?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPhone());
            ps.setString(3, account.getEmail());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    account.setId(id.getInt("id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(Account account) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE accounts SET username = ?, phone = ?, email = ? WHERE id = ?")) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPhone());
            ps.setString(3, account.getEmail());
            ps.setInt(4, account.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection conn = this.pool.getConnection();
             PreparedStatement st = conn.prepareStatement("DELETE FROM accounts WHERE id = ?;")) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}