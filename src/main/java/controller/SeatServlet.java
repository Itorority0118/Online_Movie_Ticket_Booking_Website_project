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
import java.util.*;

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

	        case "generate":
	            forward(request, response, isAjax, "seat-generate.jsp");
	            break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Seat seat = seatDAO.getSeatById(editId);
                request.setAttribute("seat", seat);
                forward(request, response, isAjax, "seat-form.jsp");
                break;

            case "list":
            default:
                String roomIdStr = request.getParameter("roomId");
                if (roomIdStr == null || roomIdStr.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/room");
                    return;
                }

                int roomId = Integer.parseInt(roomIdStr);

                List<Seat> seats = seatDAO.getSeatsByRoomId(roomId);
                Map<String, List<Seat>> seatMap = new LinkedHashMap<>();
                for (Seat s : seats) {
                    seatMap
                        .computeIfAbsent(s.getSeatRow(), k -> new ArrayList<>())
                        .add(s);
                }
                
                for (List<Seat> rowSeats : seatMap.values()) {
                    rowSeats.sort(Comparator.comparingInt(Seat::getSeatCol));
                }


                request.setAttribute("roomId", roomId);
                request.setAttribute("seatMap", seatMap);

                RoomDAO roomDAO = new RoomDAO();
                Room room = roomDAO.getRoomById(roomId);
                if (room != null) {
                    request.setAttribute("roomName", room.getRoomName());
                    request.setAttribute("cinemaId", room.getCinemaId());
                }

                if (isAjax)
                    request.getRequestDispatcher("/admin/seat-table.jsp")
                           .forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=seat-list.jsp")
                           .forward(request, response);
                break;
        }
    }

    // helper forward
    private void forward(HttpServletRequest request, HttpServletResponse response,
                         boolean isAjax, String jsp)
            throws ServletException, IOException {
        if (isAjax)
            request.getRequestDispatcher("/admin/" + jsp).forward(request, response);
        else
            request.getRequestDispatcher("/admin/dashboard.jsp?page=" + jsp)
                   .forward(request, response);
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
            response.getWriter().print("{\"success\": " + success + "}");
            return;
        }
        
        if ("generate".equals(action)) {

            int roomId = Integer.parseInt(request.getParameter("roomId"));
            char fromRow = request.getParameter("fromRow").toUpperCase().charAt(0);
            char toRow = request.getParameter("toRow").toUpperCase().charAt(0);
            int seatPerRow = Integer.parseInt(request.getParameter("seatPerRow"));
            String seatType = request.getParameter("seatType");

            List<Seat> existingSeats = seatDAO.getSeatsByRoomId(roomId);
            Map<Character, Integer> lastColInRow = new HashMap<>();
            for (Seat s : existingSeats) {
                char row = s.getSeatRow().charAt(0);
                lastColInRow.put(row, Math.max(lastColInRow.getOrDefault(row, 0), s.getSeatCol()));
            }

            int seatsAdded = 0;

            for (char row = fromRow; row <= toRow; row++) {
                int startCol = lastColInRow.getOrDefault(row, 0) + 1;
                for (int col = startCol; col < startCol + seatPerRow; col++) {

                    Seat seat = new Seat();
                    seat.setRoomId(roomId);
                    seat.setSeatRow(String.valueOf(row));
                    seat.setSeatCol(col);
                    seat.setSeatNumber(row + String.valueOf(col));
                    seat.setSeatType(seatType);
                    seat.setStatus("Available");

                    seatDAO.addSeat(seat);
                    seatsAdded++;
                }
            }

            RoomDAO roomDAO = new RoomDAO();
            Room room = roomDAO.getRoomById(roomId);
            if (room != null) {
                room.setSeatCount(room.getSeatCount() + seatsAdded);
                roomDAO.updateRoom(room);
            }

            response.sendRedirect(
                request.getContextPath() + "/seat?action=list&roomId=" + roomId
            );
            return;
        }

        String idStr = request.getParameter("id");
        String roomIdStr = request.getParameter("roomId");

        Seat seat = new Seat();
        seat.setRoomId(Integer.parseInt(roomIdStr));
        seat.setSeatRow(request.getParameter("seatRow"));
        seat.setSeatCol(Integer.parseInt(request.getParameter("seatCol")));
        seat.setSeatNumber(seat.getSeatRow() + seat.getSeatCol());
        seat.setSeatType(request.getParameter("seatType"));
        seat.setStatus(request.getParameter("status"));

        if (idStr == null || idStr.isEmpty()) {
            seatDAO.addSeat(seat);
        } else {
            seat.setSeatId(Integer.parseInt(idStr));
            seatDAO.updateSeat(seat);
        }

        response.sendRedirect(
            request.getContextPath() + "/seat?action=list&roomId=" + roomIdStr
        );
    }
}