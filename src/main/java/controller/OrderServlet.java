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
            if (session == null) {
                resp.sendError(401);
                return;
            }

            User user = (User) session.getAttribute("user");
            if (user == null) {
                resp.sendError(401);
                return;
            }
            
            String paymentMethod = req.getParameter("paymentMethod");

            try (Connection conn = DBConnection.getConnection()) {
                conn.setAutoCommit(false);

                int total;
                try {
                    total = ticketDAO.sumHoldTicketPrice(user.getUserId(), conn);
                } catch (SQLException e) {
                    conn.rollback();
                    e.printStackTrace();
                    resp.sendError(500, "Cannot calculate total");
                    return;
                }

                int orderId = orderDAO.createOrder(user.getUserId(), conn);

                if (orderId <= 0) {
                    conn.rollback();
                    resp.sendError(500);
                    return;
                }

                boolean ok = ticketDAO.confirmHoldTickets(user.getUserId(), orderId, conn);

                if (!ok) {
                    conn.rollback();
                    resp.sendError(409);
                    return;
                }

                orderDAO.updateTotalAmount(orderId, total, conn);
                orderDAO.updatePaymentMethod(orderId, paymentMethod, conn);

                List<Ticket> bookedTickets = ticketDAO.getTicketsByOrderId(orderId, conn);

                if (bookedTickets == null || bookedTickets.isEmpty()) {
                    conn.rollback();
                    resp.sendError(500, "No tickets after booking");
                    return;
                }

                // Tạo các payment record
                for (Ticket t : bookedTickets) {
                    Payment p = new Payment();
                    p.setTicketId(t.getTicketId());
                    p.setPaymentMethod(paymentMethod);
                    p.setAmount(BigDecimal.valueOf(t.getPrice()));
                    p.setPaymentDate(new Date());
                    p.setStatus("Success");

                    int paymentId = paymentDAO.createPayment(p, conn);

                    if (paymentId <= 0) {
                        conn.rollback();
                        resp.sendError(500, "Create payment failed");
                        return;
                    }
                }

                conn.commit();

                // ================= Gửi email xác nhận vé =================
                try {
                    // Lấy danh sách OrderDTO cho email
                    List<OrderDTO> orderDTOs = orderDAO.getOrderDTOByOrderId(orderId, conn);
                    utils.EmailUtil.sendTicketConfirmation(user.getEmail(), orderDTOs);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Email thất bại không block checkout, chỉ log
                }
                // ==========================================================

                resp.setContentType("application/json");
                resp.getWriter().write("{\"success\":true}");

            } catch (Exception e) {
                e.printStackTrace();
                resp.sendError(500);
            }
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