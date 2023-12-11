package ru.job4j.service;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.model.Seat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class TicketService implements Service<Seat> {
    private final BasicDataSource pool = new BasicDataSource();

    private TicketService() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(TicketService.class.getClassLoader()
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
        private static final Service<Seat> INST = new TicketService();
    }

    public static Service<Seat> instOf() {
        return TicketService.Lazy.INST;
    }

    @Override
    public Collection<Seat> findAll() {
        List<Seat> seats = new ArrayList<>();
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM places");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(new Seat(rs.getInt("id"), rs.getInt("row"),
                            rs.getInt("seat"), rs.getBoolean("bought")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        seats.sort(Comparator.comparingInt(Seat::getId));
        return seats;
    }

    @Override
    public Seat findById(int id) {
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM places WHERE id=?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Seat(id, rs.getInt("row"), rs.getInt("seat"),
                            rs.getBoolean("bought"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createTicket(int seatId, int accountId, int sessionId) {
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO tickets (seat_id, account_id, session_id) VALUES (?,?,?) ");
            ps.setInt(1, seatId);
            ps.setInt(2, accountId);
            ps.setInt(3, sessionId);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
