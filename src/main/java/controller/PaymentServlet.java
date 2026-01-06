package controller;

import java.io.IOException;
import java.util.List;

import dao.OrderDAO;
import dao.TicketDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.OrderDTO;
import model.Ticket;
import model.User;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private TicketDAO ticketDAO = new TicketDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendError(401);
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendError(401);
            return;
        }

        String showtimeIdStr = req.getParameter("showtimeId");
        if (showtimeIdStr == null) {
            resp.sendError(400);
            return;
        }

        int showtimeId = Integer.parseInt(showtimeIdStr);

        // ✅ LẤY VÉ HOLD
        List<OrderDTO> tickets =
        	    new OrderDAO().getOrdersByUser(user.getUserId())
        	        .stream()
        	        .filter(o ->
        	            "HOLD".equals(o.getStatus())
        	            && o.getShowtime().getTime() > 0 // giữ nguyên
        	        )
        	        .toList();

        int total = ticketDAO.sumTicketPrice(
            user.getUserId(),
            showtimeId
        );

        req.setAttribute("user", user);
        req.setAttribute("tickets", tickets);
        req.setAttribute("totalAmount", total);
        req.setAttribute("orderTime", new java.util.Date());

        req.getRequestDispatcher("/payment.jsp")
           .forward(req, resp);
    }
}
