package controller;

import dao.TicketDAO;
import model.Ticket;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
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

        if ("book".equals(action)) {

            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String showtimeIdStr = request.getParameter("showtimeId");
            String seatIdsStr = request.getParameter("seatIds");

            if (showtimeIdStr == null || seatIdsStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int showtimeId = Integer.parseInt(showtimeIdStr);
            String[] seatIds = seatIdsStr.split(",");

            List<Integer> seatIdList = new ArrayList<>();
            for (String seatIdStr : seatIds) {
                seatIdList.add(Integer.parseInt(seatIdStr));
            }

            List<Ticket> tickets = ticketDAO.holdTickets(
                user.getUserId(),
                showtimeId,
                seatIdList
            );
            if (tickets == null || tickets.isEmpty()) {
                response.sendError(HttpServletResponse.SC_CONFLICT, "Cannot hold seats");
                return;
            }

            request.getSession().setAttribute("tickets", tickets);

            response.setContentType("application/json");
            response.getWriter().print("{\"success\": true}");
            return;

        }

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));

            Ticket t = ticketDAO.getTicketById(id);
            boolean success = false;

            if (t != null && "Cancelled".equals(t.getStatus())) {
                success = ticketDAO.deleteTicket(id);
            }

            response.setContentType("application/json");
            response.getWriter().print("{\"success\": " + success + "}");
        }
    }
}
