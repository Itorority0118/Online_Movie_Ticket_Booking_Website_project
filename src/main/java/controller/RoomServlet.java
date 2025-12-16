package controller;

import dao.RoomDAO;
import model.Room;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet("/room")
public class RoomServlet extends HttpServlet {

    private RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                request.getRequestDispatcher("/room-form.jsp").forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Room roomToEdit = roomDAO.getRoomById(editId);
                request.setAttribute("room", roomToEdit);
                request.getRequestDispatcher("/room-form.jsp").forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                roomDAO.deleteRoom(deleteId);
                response.sendRedirect("room");
                break;

            default:
                List<Room> roomList = roomDAO.getAllRooms();
                request.setAttribute("rooms", roomList);
                request.getRequestDispatcher("/room-list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String cinemaIdStr = request.getParameter("cinemaId");
        String roomName = request.getParameter("roomName");
        String seatCountStr = request.getParameter("seatCount");
        String roomType = request.getParameter("roomType");

        Room room = new Room();
        room.setCinemaId(Integer.parseInt(cinemaIdStr));
        room.setRoomName(roomName);
        room.setSeatCount(Integer.parseInt(seatCountStr));
        room.setRoomType(roomType);

        if (idStr == null || idStr.isEmpty()) {
            // Add new room
            roomDAO.addRoom(room);
        } else {
            // Update existing room
            room.setRoomId(Integer.parseInt(idStr));
            roomDAO.updateRoom(room);
        }
        response.sendRedirect("room");
    }
}