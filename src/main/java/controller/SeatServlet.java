package controller;

import dao.RoomDAO;
import dao.SeatDAO;
import dao.ShowtimeDAO;
import model.Room;
import model.Seat;
import model.Showtime;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;

@SuppressWarnings("serial")
@WebServlet("/seat")
public class SeatServlet extends HttpServlet {

    private SeatDAO seatDAO = new SeatDAO();
    private ShowtimeDAO showTimeDAO = new ShowtimeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String action = request.getParameter("action");
        if (action == null) action = "list";

        request.setAttribute("activeSidebar", "seat");

        switch (action) {

            case "generate":
                String roomIdStr = request.getParameter("roomId");
                String showtimeIdStr = request.getParameter("showtimeId");

                if ((roomIdStr == null || roomIdStr.isEmpty()) && showtimeIdStr != null && !showtimeIdStr.isEmpty()) {
                    int showtimeId = Integer.parseInt(showtimeIdStr);
                    Showtime showtime = showTimeDAO.getShowtimeById(showtimeId);
                    if (showtime == null) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid showtimeId");
                        return;
                    }
                    roomIdStr = String.valueOf(showtime.getRoomId());
                }

                if (roomIdStr == null || roomIdStr.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing roomId or showtimeId");
                    return;
                }

                request.setAttribute("roomId", roomIdStr);
                request.setAttribute("showtimeId", showtimeIdStr);
                forward(request, response, isAjax, "seat-generate.jsp");
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Seat seat = seatDAO.getSeatById(editId);
                request.setAttribute("seat", seat);
                forward(request, response, isAjax, "seat-form.jsp");
                break;
                
            case "byShowtime":
                int showtimeId = Integer.parseInt(request.getParameter("showtimeId"));

                List<Seat> seats = seatDAO.getSeatsByShowtime(showtimeId);

                response.setContentType("application/json;charset=UTF-8");
                new Gson().toJson(seats, response.getWriter());
                break;

            case "list":
            default:
                handleList(request, response, isAjax);
                break;
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response, boolean isAjax)
            throws ServletException, IOException {

        String showtimeIdStr = request.getParameter("showtimeId");
        int roomId;

        if(showtimeIdStr != null && !showtimeIdStr.isEmpty()) {
            int showtimeId = Integer.parseInt(showtimeIdStr);
            Showtime showtime = showTimeDAO.getShowtimeById(showtimeId);
            if(showtime == null) {
                response.sendRedirect(request.getContextPath() + "/admin/showtime");
                return;
            }
            roomId = showtime.getRoomId();
            request.setAttribute("showtimeId", showtimeId);
        } else {
            String roomIdStr = request.getParameter("roomId");
            if(roomIdStr == null || roomIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/room");
                return;
            }
            roomId = Integer.parseInt(roomIdStr);
        }

        List<Seat> seats = seatDAO.getSeatsByRoomId(roomId);
        Map<String, List<Seat>> seatMap = new LinkedHashMap<>();
        for (Seat s : seats) {
            seatMap.computeIfAbsent(s.getSeatRow(), k -> new ArrayList<>()).add(s);
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
            request.getRequestDispatcher("/admin/seat-table.jsp").forward(request, response);
        else
            request.getRequestDispatcher("/admin/dashboard.jsp?page=seat-list.jsp").forward(request, response);
    }

    // helper forward
    private void forward(HttpServletRequest request, HttpServletResponse response,
                         boolean isAjax, String jsp)
            throws ServletException, IOException {
        if (isAjax)
            request.getRequestDispatcher("/admin/" + jsp).forward(request, response);
        else
            request.getRequestDispatcher("/admin/dashboard.jsp?page=" + jsp).forward(request, response);
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

            String roomIdStr = request.getParameter("roomId");
            String showtimeIdStr = request.getParameter("showtimeId");
            int roomId;

            if (roomIdStr != null && !roomIdStr.isEmpty()) {
                roomId = Integer.parseInt(roomIdStr);
            } else if (showtimeIdStr != null && !showtimeIdStr.isEmpty()) {
                int showtimeId = Integer.parseInt(showtimeIdStr);
                Showtime showtime = showTimeDAO.getShowtimeById(showtimeId);
                if (showtime == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid showtimeId");
                    return;
                }
                roomId = showtime.getRoomId();
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing roomId or showtimeId");
                return;
            }

            char fromRow = request.getParameter("fromRow").toUpperCase().charAt(0);
            char toRow = request.getParameter("toRow").toUpperCase().charAt(0);
            int seatPerRow = Integer.parseInt(request.getParameter("seatPerRow"));
            String seatType = request.getParameter("seatType");
            
            Map<String, String> errors = new HashMap<>();

	         if (fromRow < 'A' || fromRow > 'Z') {
	             errors.put("fromRow", "From Row must be a letter from A to Z");
	         }
	
	         if (toRow < 'A' || toRow > 'Z') {
	             errors.put("toRow", "To Row must be a letter from A to Z");
	         }
	
	         if (fromRow > toRow) {
	             errors.put("rowOrder", "From Row must come before To Row (e.g. A → H)");
	         }
	
	         if (seatPerRow <= 0) {
	             errors.put("seatPerRow", "Seats per row must be greater than 0");
	         }
	
	         if (seatType == null || seatType.isEmpty()) {
	             errors.put("seatType", "Seat Type is required");
	         }
	         
	         if (!errors.isEmpty()) {
	        	    request.setAttribute("errors", errors);
	        	    request.setAttribute("roomId", roomId);
	        	    request.setAttribute("showtimeId", showtimeIdStr);
	        	    request.getRequestDispatcher("/admin/dashboard.jsp?page=seat-generate.jsp")
	        	           .forward(request, response);
	        	    return;
	         }

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

            response.sendRedirect(request.getContextPath() + "/seat?action=list&roomId=" + roomId);
            return;
        }

        String idStr = request.getParameter("id");
        String roomIdStr = request.getParameter("roomId");
        String seatRow = request.getParameter("seatRow");
        String seatColStr = request.getParameter("seatCol");
        String seatType = request.getParameter("seatType");
        String status = request.getParameter("status");

        Map<String, String> errors = new HashMap<>();

        if (seatRow == null || seatRow.isEmpty()) errors.put("seatRow", "Seat Row is required");
        if (seatColStr == null || seatColStr.isEmpty()) errors.put("seatCol", "Seat Column is required");
        if (seatType == null || seatType.isEmpty()) errors.put("seatType", "Seat Type is required");
        if (status == null || status.isEmpty()) errors.put("status", "Status is required");

        int seatCol = 0;
        try {
            if (seatColStr != null && !seatColStr.isEmpty()) {
                seatCol = Integer.parseInt(seatColStr);
                if (seatCol <= 0) errors.put("seatCol", "Seat Column must be positive");
            }
        } catch (NumberFormatException e) {
            errors.put("seatCol", "Seat Column must be a number");
        }

        Seat seat = new Seat();
        seat.setRoomId(Integer.parseInt(roomIdStr));
        seat.setSeatRow(seatRow != null ? seatRow : "");
        seat.setSeatCol(seatCol);
        seat.setSeatNumber(seatRow + seatCol);
        seat.setSeatType(seatType);
        seat.setStatus(status);

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("seat", seat);
            request.getRequestDispatcher("/admin/dashboard.jsp?page=seat-form.jsp")
                   .forward(request, response);
            return;
        }

        if (idStr == null || idStr.isEmpty()) {
            seatDAO.addSeat(seat);
        } else {
            seat.setSeatId(Integer.parseInt(idStr));
            seatDAO.updateSeat(seat);
        }

        response.sendRedirect(request.getContextPath() + "/seat?action=list&roomId=" + roomIdStr);
    }
}