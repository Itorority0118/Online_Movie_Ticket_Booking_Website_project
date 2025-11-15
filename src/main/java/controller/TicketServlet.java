package controller;

import dao.TicketDAO;
import model.Ticket;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet("/ticket")
public class TicketServlet extends HttpServlet {

    private TicketDAO ticketDAO = new TicketDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "cancel":
                int ticketId = Integer.parseInt(request.getParameter("id"));
                ticketDAO.cancelTicket(ticketId);
                response.sendRedirect("ticket");
                break;

            default:
                String userIdStr = request.getParameter("userId");
                if (userIdStr != null) {
                    try {
                        int userId = Integer.parseInt(userIdStr);
                        List<Ticket> ticketList = ticketDAO.getTicketsByUser(userId);
                        request.setAttribute("tickets", ticketList);
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "Invalid user ID.");
                    }
                } else {
                    request.setAttribute("error", "Missing userId parameter.");
                }
                request.getRequestDispatcher("/ticket-list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int showtimeId = Integer.parseInt(request.getParameter("showtimeId"));
            int seatId = Integer.parseInt(request.getParameter("seatId"));
            double price = Double.parseDouble(request.getParameter("price"));
            String bookingTime = request.getParameter("bookingTime");
            String status = request.getParameter("status");

            Ticket ticket = new Ticket();
            ticket.setUserId(userId);
            ticket.setShowtimeId(showtimeId);
            ticket.setSeatId(seatId);
            ticket.setPrice(price);
            ticket.setBookingTime(bookingTime);
            ticket.setStatus(status);

            ticketDAO.bookTicket(ticket);

        } catch (Exception e) {
            request.setAttribute("error", "Failed to book ticket: " + e.getMessage());
        }

        response.sendRedirect("ticket?userId=" + request.getParameter("userId"));
    }
}
