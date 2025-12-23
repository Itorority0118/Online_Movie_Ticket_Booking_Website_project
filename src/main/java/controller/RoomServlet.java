package controller;

import dao.CinemaDAO;
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

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String action = request.getParameter("action");
        if (action == null) action = "list";

        request.setAttribute("activeSidebar", "room");

        switch (action) {

            case "new":
                request.setAttribute("room", null);

                if (isAjax)
                    request.getRequestDispatcher("/admin/room-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=room-form.jsp")
                           .forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Room room = roomDAO.getRoomById(editId);
                request.setAttribute("room", room);

                if (isAjax)
                    request.getRequestDispatcher("/admin/room-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=room-form.jsp")
                           .forward(request, response);
                break;

            case "list":
            default:
                String cinemaIdStr = request.getParameter("cinemaId");
                String keyword = request.getParameter("keyword");
                String roomType = request.getParameter("roomType");

                Integer cinemaId = (cinemaIdStr != null && !cinemaIdStr.isEmpty())
                                    ? Integer.parseInt(cinemaIdStr) : null;

                // Lấy danh sách room theo filter
                List<Room> rooms = roomDAO.searchRooms(cinemaId, keyword, roomType);

                if (cinemaId != null) {
                    CinemaDAO cinemaDAO = new CinemaDAO();
                    var cinema = cinemaDAO.getCinemaById(cinemaId);
                    if (cinema != null) request.setAttribute("cinemaName", cinema.getName());
                    else request.setAttribute("cinemaName", "Unknown Cinema");

                    request.setAttribute("cinemaId", cinemaId);
                }

                request.setAttribute("rooms", rooms);

                if (isAjax)
                    request.getRequestDispatcher("/admin/room-table.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=room-list.jsp")
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
            boolean success = roomDAO.deleteRoom(id);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("{\"success\": " + success + "}");
            return;
        }
        
        String idStr = request.getParameter("id");
        String cinemaIdStr = request.getParameter("cinemaId");
        String roomName = request.getParameter("roomName");
        String roomType = request.getParameter("roomType");

        Room room = new Room();
        room.setCinemaId(Integer.parseInt(cinemaIdStr));
        room.setRoomName(roomName);
        room.setRoomType(roomType);
        room.setSeatCount(0);
        if (idStr == null || idStr.isEmpty()) {
            roomDAO.addRoom(room);
        } else {
            room.setRoomId(Integer.parseInt(idStr));
            roomDAO.updateRoom(room);
        }
        response.sendRedirect(request.getContextPath() + "/room?action=list&cinemaId=" + cinemaIdStr);
    }
}