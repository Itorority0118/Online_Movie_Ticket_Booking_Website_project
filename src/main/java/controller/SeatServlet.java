package controller;

import dao.SeatDAO;
import model.Seat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet("/seat")
public class SeatServlet extends HttpServlet {

    private SeatDAO seatDAO = new SeatDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "edit":
                int seatId = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("seatId", seatId);
                request.getRequestDispatcher("/seat-form.jsp").forward(request, response);
                break;

            default:
                String showtimeIdStr = request.getParameter("showtimeId");
                if (showtimeIdStr != null) {
                    try {
                        int showtimeId = Integer.parseInt(showtimeIdStr);
                        List<Seat> seatList = seatDAO.getSeatsByShowtime(showtimeId);
                        request.setAttribute("seats", seatList);
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "Invalid showtime ID.");
                    }
                } else {
                    request.setAttribute("error", "Missing showtimeId parameter.");
                }
                request.getRequestDispatcher("/seat-list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String seatIdStr = request.getParameter("seatId");
        String status = request.getParameter("status");

        if (seatIdStr != null && status != null) {
            try {
                int seatId = Integer.parseInt(seatIdStr);
                boolean success = seatDAO.updateSeatStatus(seatId, status);
                if (!success) {
                    request.setAttribute("error", "Failed to update seat status.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid seat ID.");
            }
        } else {
            request.setAttribute("error", "Missing update information.");
        }

        response.sendRedirect("seat?showtimeId=" + request.getParameter("showtimeId"));
    }
}