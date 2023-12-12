package ru.job4j.servlet;

import ru.job4j.model.Account;
import ru.job4j.model.Seat;
import ru.job4j.model.Ticket;
import ru.job4j.service.AccountService;
import ru.job4j.service.SeatService;
import ru.job4j.service.Service;
import ru.job4j.service.TicketService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PaymentServlet extends HttpServlet {
    private final SeatService seatService = SeatService.instOf();
    private final AccountService accountService = AccountService.instOf();
    private final TicketService ticketService = TicketService.instOf();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        int seatId = Integer.parseInt(req.getReader().readLine());
        Seat seat = seatService.findById(seatId);
        if (!seat.isBought()) {
            Account account = accountService.createAccount(new Account(0, (String) req.getAttribute("username"),
                    (String) req.getAttribute("email"),
                    (String) req.getAttribute("phone")));
            if (account.getId() != 0) {
                ticketService.createTicket(new Ticket(0, seatId, account.getId(),
                        Integer.parseInt(req.getSession().getId())));
                seatService.boughtSeat(seatId);
            }
        } else {
            resp.sendError(1);
        }
    }
}
