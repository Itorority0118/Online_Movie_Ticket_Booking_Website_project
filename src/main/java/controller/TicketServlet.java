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

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String action = request.getParameter("action");
        if (action == null) action = "list";

        request.setAttribute("activeSidebar", "ticket");

        switch (action) {

            case "new":
                request.setAttribute("ticket", null);

                if (isAjax)
                    request.getRequestDispatcher("/admin/ticket-form.jsp")
                           .forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=ticket-form.jsp")
                           .forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Ticket ticket = ticketDAO.getTicketById(editId);
                request.setAttribute("ticket", ticket);

                if (isAjax)
                    request.getRequestDispatcher("/admin/ticket-form.jsp")
                           .forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=ticket-form.jsp")
                           .forward(request, response);
                break;

            case "cancel":
                int cancelId = Integer.parseInt(request.getParameter("id"));
                ticketDAO.cancelTicket(cancelId);

                response.sendRedirect(request.getContextPath() + "/ticket?action=list");
                break;

            case "list":
            default: {

                String userIdStr = request.getParameter("userId");
                String showtimeIdStr = request.getParameter("showtimeId");
                String status = request.getParameter("status");

                Integer userId = null;
                Integer showtimeId = null;

                if (userIdStr != null && !userIdStr.isEmpty()) {
                    userId = Integer.parseInt(userIdStr);
                }

                if (showtimeIdStr != null && !showtimeIdStr.isEmpty()) {
                    showtimeId = Integer.parseInt(showtimeIdStr);
                }

                List<Ticket> tickets = ticketDAO.filterTickets(
                        userId,
                        showtimeId,
                        status
                );

                request.setAttribute("tickets", tickets);

                if (isAjax) {
                    request.getRequestDispatcher("/admin/ticket-table.jsp")
                           .forward(request, response);
                } else {
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=ticket-list.jsp")
                           .forward(request, response);
                }
                break;
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));

            Ticket t = ticketDAO.getTicketById(id);
            boolean success = false;

            if (t != null && "Cancelled".equals(t.getStatus())) {
                success = ticketDAO.deleteTicket(id);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("{\"success\": " + success + "}");
            return;
        }

        String ticketIdStr = request.getParameter("ticketId");

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

        if (ticketIdStr == null || ticketIdStr.isEmpty()) {
            // ADD
            ticketDAO.bookTicket(ticket);
        } else {
            // UPDATE
            ticket.setTicketId(Integer.parseInt(ticketIdStr));
            ticketDAO.updateTicket(ticket);
        }

        response.sendRedirect(request.getContextPath() + "/ticket?action=list");
    }
}
