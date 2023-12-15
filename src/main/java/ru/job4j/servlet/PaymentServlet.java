package ru.job4j.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.model.Account;
import ru.job4j.model.Seat;
import ru.job4j.model.Ticket;
import ru.job4j.service.AccountService;
import ru.job4j.service.SeatService;
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
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        int seatId = Integer.parseInt(req.getParameter("seatId"));
        Seat seat = seatService.findById(seatId);
        if (!seat.isBought()) {
            Account account = accountService.saveAccount(new Account(0, req.getParameter("username"),
                    req.getParameter("email"), req.getParameter("phone")));
            if (account.getId() != 0) {
                Ticket ticket = ticketService.buyTicket(new Ticket(0, seatId, account.getId(),
                        req.getSession().getId()));
                seatService.buySeat(seatId);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(ticket);
                resp.getWriter().write(json);
            }
        } else {
            resp.sendError(1, "fail");
        }
    }
}
