package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import dao.MovieDAO;
import dao.TicketDAO;
import dao.UserDAO;
import dao.PaymentDAO;

@WebServlet("/admin")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    UserDAO userDAO = new UserDAO();
    MovieDAO movieDAO = new MovieDAO();
    TicketDAO ticketDAO = new TicketDAO();
    PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int totalUsers = userDAO.countUsers();
        int totalAdmins = userDAO.countUsersByRole("admin");
        int totalActive = userDAO.countUsersByStatus("active");
        int totalInactive = userDAO.countUsersByStatus("inactive");

        int totalMovies = movieDAO.countMovies();
        int totalTickets = ticketDAO.countSoldTickets();

        int totalUsed = ticketDAO.countTicketsByStatus("Used");
        int totalBooked = ticketDAO.countTicketsByStatus("Booked");
        int totalHold = ticketDAO.countTicketsByStatus("HOLD");
        int totalCancelled = ticketDAO.countTicketsByStatus("Cancelled");

        double totalRevenue = paymentDAO.getTotalRevenue();
        double totalRevenueSuccess = paymentDAO.getRevenueByStatus("Success");
        double totalRevenueFailed = paymentDAO.getRevenueByStatus("Failed");
        double totalRevenuePending = paymentDAO.getRevenueByStatus("Pending");

        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("totalAdmins", totalAdmins);
        req.setAttribute("totalActive", totalActive);
        req.setAttribute("totalInactive", totalInactive);

        req.setAttribute("totalMovies", totalMovies);
        req.setAttribute("totalTickets", totalTickets);
        req.setAttribute("totalUsed", totalUsed);
        req.setAttribute("totalBooked", totalBooked);
        req.setAttribute("totalHold", totalHold);
        req.setAttribute("totalCancelled", totalCancelled);

        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("totalRevenueSuccess", totalRevenueSuccess);
        req.setAttribute("totalRevenueFailed", totalRevenueFailed);
        req.setAttribute("totalRevenuePending", totalRevenuePending);

        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}