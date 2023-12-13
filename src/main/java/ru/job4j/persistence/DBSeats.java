package ru.job4j.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.model.Seat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

// 	Persistence -  постоянное соединение (в Java EE – протокол передачи состояния сущностного объекта EJB
// 	между его экземплярами переменных и основной базой данных ssn);
// взаимодействие с базой данных (из кн.: Шефер К., Хо К., Харроп Р. Spring 4 для профессионалов Alex_Odeychuk);
public class DBSeats implements Repository<Seat> {
    private final BasicDataSource pool = new BasicDataSource();

    private DBSeats() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(DBSeats.class.getClassLoader()
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
        private static final DBSeats INST = new DBSeats();
    }

    public static DBSeats instOf() {
        return DBSeats.Lazy.INST;
    }

    @Override
    public Collection<Seat> findAll() {
        List<Seat> seats = new ArrayList<>();
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM seats");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(new Seat(rs.getInt("id"), rs.getInt("row"),
                            rs.getInt("cell"), rs.getBoolean("bought")));
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
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM seats WHERE id=?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Seat(id, rs.getInt("row"), rs.getInt("cell"),
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

    public boolean soldSeat(int id) {
        boolean result = false;
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement("UPDATE seats SET bought=true WHERE id=?");
            ps.setInt(1, id);
            result = ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
