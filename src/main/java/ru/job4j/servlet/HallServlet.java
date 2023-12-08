package ru.job4j.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.model.Seat;
import ru.job4j.service.Service;
import ru.job4j.service.TicketService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class HallServlet extends HttpServlet {
    private final Service<Seat> service = TicketService.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        List<Seat> places = (List<Seat>) service.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        String stringOfSeats = objectMapper.writeValueAsString(places);
        resp.getWriter().write(stringOfSeats);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        int seatId = Integer.parseInt(req.getReader().readLine());
        Seat seat = service.findById(seatId);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(seat);
        resp.getWriter().print(json);
    }
}