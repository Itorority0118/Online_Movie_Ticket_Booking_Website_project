package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import dao.OrderDAO;
import dao.PaymentDAO;
import dao.TicketDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.OrderDTO;
import model.Payment;
import model.Ticket;
import model.User;
import utils.DBConnection;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    private TicketDAO ticketDAO = new TicketDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    	String action = req.getParameter("action");

    	HttpSession session = req.getSession(false);
    	if (session == null || session.getAttribute("user") == null) {
    	    resp.setContentType("application/json");
    	    resp.setCharacterEncoding("UTF-8");
    	    resp.getWriter().write("[]");
    	    return;
    	}

    	User user = (User) session.getAttribute("user");

    	if ("ajax".equals(action)) {
    	    resp.setContentType("application/json");
    	    resp.setCharacterEncoding("UTF-8");
    	    List<HashMap<String, Object>> result = ticketDAO.getHoldingTicketDetails(user.getUserId());
    	    resp.getWriter().write(new Gson().toJson(result));
    	    return;
    	}

    	if ("myTickets".equals(action)) {
    	    resp.setContentType("application/json");
    	    resp.setCharacterEncoding("UTF-8");
    	    List<HashMap<String, Object>> result = ticketDAO.getBookedTicketsByUser(user.getUserId());
    	    resp.getWriter().write(new Gson().toJson(result));
    	    return;
    	}

    	resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("addToCart".equals(action)) {

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

            int showtimeId = parseIntSafe(req.getParameter("showtimeId"));
            if (showtimeId <= 0) {
                resp.sendError(400, "Missing showtimeId");
                return;
            }

            String seatIdsStr = req.getParameter("seatIds");

            List<Integer> seatIds = new ArrayList<>();
            for (String s : seatIdsStr.split(",")) {
                seatIds.add(Integer.parseInt(s));
            }

            List<Ticket> tickets = ticketDAO.holdTickets(
                user.getUserId(),
                showtimeId,
                seatIds
            );

            if (tickets == null || tickets.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }

            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\":true}");
        }
        
        if ("checkout".equals(action)) {

            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                resp.sendError(401);
                return;
            }

            User user = (User) session.getAttribute("user");
            String paymentMethod = req.getParameter("paymentMethod");

            String ticketIdsStr = req.getParameter("ticketIds");
            if (ticketIdsStr == null || ticketIdsStr.isEmpty()) {
                resp.sendError(400, "Missing ticketIds");
                return;
            }

            List<Integer> ticketIds = new ArrayList<>();
            for (String s : ticketIdsStr.split(",")) {
                ticketIds.add(Integer.parseInt(s));
            }

            int orderId = -1;

            try (Connection conn = DBConnection.getConnection()) {
                conn.setAutoCommit(false);

                int total = ticketDAO.sumHoldTicketPriceByIds(
                        user.getUserId(),
                        ticketIds,
                        conn
                );

                if (total <= 0) {
                    conn.rollback();
                    resp.sendError(400, "Invalid total");
                    return;
                }

                orderId = orderDAO.createOrder(user.getUserId(), conn);
                if (orderId <= 0) {
                    conn.rollback();
                    resp.sendError(500, "Create order failed");
                    return;
                }

                boolean ok = ticketDAO.confirmHoldTicketsByIds(
                        user.getUserId(),
                        ticketIds,
                        orderId,
                        conn
                );

                if (!ok) {
                    conn.rollback();
                    resp.sendError(409, "Ticket confirm failed");
                    return;
                }

                orderDAO.updateTotalAmount(orderId, total, conn);
                orderDAO.updatePaymentMethod(orderId, paymentMethod, conn);

                List<Ticket> bookedTickets =
                        ticketDAO.getTicketsByOrderId(orderId, conn);

                for (Ticket t : bookedTickets) {
                    Payment p = new Payment();
                    p.setTicketId(t.getTicketId());
                    p.setPaymentMethod(paymentMethod);
                    p.setAmount(BigDecimal.valueOf(t.getPrice()));
                    p.setPaymentDate(new Date());
                    p.setStatus("Success");

                    if (paymentDAO.createPayment(p, conn) <= 0) {
                        conn.rollback();
                        resp.sendError(500, "Create payment failed");
                        return;
                    }
                }

                conn.commit();

            } catch (Exception e) {
                e.printStackTrace();
                resp.sendError(500, "Checkout failed");
                return;
            }

            // ✅ GỬI MAIL = CONNECTION MỚI (SAU COMMIT)
            try (Connection mailConn = DBConnection.getConnection()) {
                List<OrderDTO> orderDTOs =
                        orderDAO.getOrderDTOByOrderId(orderId, mailConn);

                utils.EmailUtil.sendTicketConfirmation(
                        user.getEmail(),
                        orderDTOs
                );
            } catch (Exception e) {
                e.printStackTrace(); // log thôi, không ảnh hưởng checkout
            }

            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\":true}");
        }
        
        if ("cancelHold".equals(action)) {

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

            int ticketId = parseIntSafe(req.getParameter("ticketId"));
            if (ticketId <= 0) {
                resp.sendError(400, "Missing ticketId");
                return;
            }

            boolean ok = orderDAO.deleteHoldTicket(
                ticketId,
                user.getUserId()
            );

            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\":" + ok + "}");
        }
    }
    private int parseIntSafe(String v) {
        if (v == null || v.equals("undefined") || v.isEmpty()) return -1;
        return Integer.parseInt(v);
    }

}