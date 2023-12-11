package ru.job4j.servlet;

import ru.job4j.model.Account;
import ru.job4j.model.Seat;
import ru.job4j.service.AccountService;
import ru.job4j.service.Service;
import ru.job4j.service.TicketService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PaymentServlet extends HttpServlet {
    private final Service<Seat> seatService = TicketService.instOf();
    private final AccountService accountService = AccountService.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        int seatId = Integer.parseInt(req.getReader().readLine());
        Seat seat = seatService.findById(seatId);
        if (!seat.isBought()) {
            if (accountService.createAccount(
                    (String) req.getAttribute("username"), (String) req.getAttribute("email"),
                    (String) req.getAttribute("phone"))) {
                //something
            }
        } else {
            //error
        }
    }
}
