package ru.job4j.service;

import ru.job4j.model.Ticket;
import ru.job4j.persistence.DBTickets;

import java.util.Collection;

public class TicketService implements Service<Ticket> {
    private final DBTickets ticketsFromDB = DBTickets.instOf();

    private static final class Lazy {
        private static final TicketService INST = new TicketService();
    }

    public static TicketService instOf() {
        return TicketService.Lazy.INST;
    }

    @Override
    public Collection<Ticket> findAll() {
        return ticketsFromDB.findAll();
    }

    @Override
    public Ticket findById(int id) {
        return ticketsFromDB.findById(id);
    }

    public Ticket buyTicket(Ticket ticket) {
        return ticketsFromDB.save(ticket);
    }
}
