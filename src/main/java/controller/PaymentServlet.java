package controller;

import java.io.IOException;
import java.util.List;

import dao.OrderDAO;
import dao.PaymentDAO;
import dao.TicketDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.OrderDTO;
import model.Payment;
import model.User;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private TicketDAO ticketDAO = new TicketDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String action = request.getParameter("action");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User user = (User) session.getAttribute("user");

        // ========================= ADMIN PAYMENT =========================
        if (action != null) {

            request.setAttribute("activeSidebar", "payment");

            switch (action) {

                case "view": {
                    String idStr = request.getParameter("id");
                    if (idStr == null) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }

                    int id = Integer.parseInt(idStr);
                    Payment payment = paymentDAO.getPaymentById(id);

                    if (payment == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }

                    request.setAttribute("payment", payment);
                    request.getRequestDispatcher("/admin/payment-detail.jsp")
                           .forward(request, response);
                    return;
                }

                case "list":
                default: {
                    String status = request.getParameter("status");
                    String ticketIdStr = request.getParameter("ticketId");
                    String method = request.getParameter("method");

                    Integer ticketId = null;
                    if (ticketIdStr != null && !ticketIdStr.isEmpty()) {
                        try {
                            ticketId = Integer.parseInt(ticketIdStr);
                        } catch (NumberFormatException e) {
                            ticketId = null;
                        }
                    }

                    List<Payment> payments = paymentDAO.filterPayments(ticketId, status, method);
                    request.setAttribute("payments", payments);

                    if (isAjax) {
                        request.getRequestDispatcher("/admin/payment-table.jsp")
                               .forward(request, response);
                    } else {
                        request.getRequestDispatcher(
                            "/admin/dashboard.jsp?page=payment-list.jsp"
                        ).forward(request, response);
                    }
                    return;
                }
            }
        }

        // ========================= USER PAYMENT PAGE =========================
        String showtimeIdStr = request.getParameter("showtimeId");
        if (showtimeIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int showtimeId = Integer.parseInt(showtimeIdStr);

        List<OrderDTO> tickets =
            new OrderDAO().getOrdersByUser(user.getUserId())
                .stream()
                .filter(o ->
                    "HOLD".equals(o.getStatus()) &&
                    o.getShowtime().getTime() > 0
                )
                .toList();

        int total = ticketDAO.sumTicketPrice(
            user.getUserId(),
            showtimeId
        );

        request.setAttribute("user", user);
        request.setAttribute("tickets", tickets);
        request.setAttribute("totalAmount", total);
        request.setAttribute("orderTime", new java.util.Date());

        request.getRequestDispatcher("/payment.jsp")
               .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("updateStatus".equals(action)) {

            int paymentId = Integer.parseInt(request.getParameter("id"));
            String status = request.getParameter("status");

            boolean success = paymentDAO.updatePaymentStatus(paymentId, status);

            response.setContentType("application/json");
            response.getWriter().print("{\"success\": " + success + "}");
        }
        
        if ("delete".equals(action)) {

            int id = Integer.parseInt(request.getParameter("id"));

            Payment p = paymentDAO.getPaymentById(id);
            boolean success = false;

            if (p != null && "Failed".equals(p.getStatus())) {
                success = paymentDAO.deletePayment(id);
            }

            response.setContentType("application/json");
            response.getWriter().print("{\"success\": " + success + "}");
        }
    }
}