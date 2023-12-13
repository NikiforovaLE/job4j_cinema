package ru.job4j.service;

import ru.job4j.model.Seat;
import ru.job4j.persistence.DBSeats;

import java.util.*;

public class SeatService implements Service<Seat> {
    private final DBSeats seatsFromDB = DBSeats.instOf();

    private static final class Lazy {
        private static final SeatService INST = new SeatService();
    }

    public static SeatService instOf() {
        return SeatService.Lazy.INST;
    }

    @Override
    public Collection<Seat> findAll() {
        return seatsFromDB.findAll();
    }

    @Override
    public Seat findById(int id) {
        return seatsFromDB.findById(id);
    }

    public boolean buySeat(int id) {
        return seatsFromDB.soldSeat(id);
    }
}
