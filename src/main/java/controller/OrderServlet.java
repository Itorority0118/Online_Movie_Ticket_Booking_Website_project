package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import dao.OrderDAO;
import dao.TicketDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Ticket;
import model.User;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    private TicketDAO ticketDAO = new TicketDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (!"ajax".equals(action)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.getWriter().write("[]");
            return;
        }

        User user = (User) session.getAttribute("user");

        // ✅ LẤY HOLD TICKET + MOVIE + SEAT + HOLD TIME
        List<HashMap<String, Object>> result =
                ticketDAO.getHoldingTicketDetails(user.getUserId());

        resp.getWriter().write(new Gson().toJson(result));
    }


    // ============================
    // ADD TO CART = HOLD GHẾ
    // ============================
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

            int showtimeId = Integer.parseInt(req.getParameter("showtimeId"));
            String paymentMethod = req.getParameter("paymentMethod");

            // 1️⃣ TÍNH TỔNG TIỀN (KHI CÒN HOLD)
            int total = ticketDAO.sumTicketPrice(
                user.getUserId(),
                showtimeId
            );

            if (total <= 0) {
                resp.sendError(409, "No ticket to pay");
                return;
            }

            // 2️⃣ TẠO ORDER
            int orderId = orderDAO.createOrder(user.getUserId());
            if (orderId <= 0) {
                resp.sendError(500);
                return;
            }

            // 3️⃣ HOLD → BOOKED + gán OrderId
            boolean ok = ticketDAO.confirmHoldTickets(
                user.getUserId(),
                showtimeId,
                orderId
            );

            if (!ok) {
                resp.sendError(409);
                return;
            }

            // 4️⃣ UPDATE ORDER
            orderDAO.updateTotalAmount(orderId, total);
            orderDAO.updatePaymentMethod(orderId, paymentMethod);
            // orderDAO.updatePaymentStatus(orderId, "PAID"); // nếu có cột

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