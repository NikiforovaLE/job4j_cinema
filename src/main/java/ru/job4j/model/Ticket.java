package ru.job4j.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Ticket {
    private Seat seat;
    private Account account;
}
