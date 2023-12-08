package ru.job4j.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class Seat {
    private int id;
    private final int row;
    private final int seat;
    private boolean bought;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return id == seat.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
