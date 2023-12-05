package ru.job4j.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat {
    private int id;
    private int idUser;
    private final int row;
    private final int seat;
    private boolean isBought;

    public Seat(int row, int seat) {
        this.row = row;
        this.seat = seat;
    }
}
