package controller;

import dao.RoomDAO;
import dao.SeatDAO;
import model.Room;
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

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String action = request.getParameter("action");
        if (action == null) action = "list";

        request.setAttribute("activeSidebar", "seat");

        switch (action) {
            case "new":
                request.setAttribute("seat", null);

                if (isAjax)
                    request.getRequestDispatcher("/admin/seat-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=seat-form.jsp")
                           .forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Seat seat = seatDAO.getSeatById(editId);
                request.setAttribute("seat", seat);

                if (isAjax)
                    request.getRequestDispatcher("/admin/seat-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=seat-form.jsp")
                           .forward(request, response);
                break;

            case "list":
            default:
                String roomIdStr = request.getParameter("roomId");
                String keyword = request.getParameter("keyword");
                String status = request.getParameter("status");
                String seatType = request.getParameter("seatType");
                Integer roomId = (roomIdStr != null && !roomIdStr.isEmpty()) 
                                    ? Integer.parseInt(roomIdStr) : null;

                List<Seat> seats;
                if (roomId != null) {
                    seats = seatDAO.getSeatsByRoomId(roomId, keyword, status, seatType);
                    request.setAttribute("roomId", roomId);
                    RoomDAO roomDAO = new RoomDAO();
                    Room room = roomDAO.getRoomById(roomId);
                    if (room != null) {
                        request.setAttribute("roomName", room.getRoomName());
                        request.setAttribute("cinemaId", room.getCinemaId());
                    } else {
                        request.setAttribute("roomName", "Unknown Room");
                    }
                } else {
                    seats = seatDAO.getAllSeats();
                }

                request.setAttribute("seats", seats);

                if (isAjax)
                    request.getRequestDispatcher("/admin/seat-table.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=seat-list.jsp")
                           .forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = seatDAO.deleteSeat(id);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("{\"success\": " + success + "}");
            return;
        }

        String idStr = request.getParameter("id");
        String roomIdStr = request.getParameter("roomId");
        String seatNumber = request.getParameter("seatNumber");
        String status = request.getParameter("status");
        String seatType = request.getParameter("seatType");

        Seat seat = new Seat();
        seat.setRoomId(Integer.parseInt(roomIdStr));
        seat.setSeatNumber(seatNumber);
        seat.setStatus(status);
        seat.setSeatType(seatType);

        if (idStr == null || idStr.isEmpty()) {
            // Add new seat
            seatDAO.addSeat(seat);
        } else {
            // Update existing seat
            seat.setSeatId(Integer.parseInt(idStr));
            seatDAO.updateSeat(seat);
        }

        response.sendRedirect(request.getContextPath() + "/seat?action=list&roomId=" + roomIdStr);
    }
}
