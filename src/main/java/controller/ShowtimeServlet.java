package controller;

import dao.ShowtimeDAO;
import model.Showtime;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet("/showtime")
public class ShowtimeServlet extends HttpServlet {

    private ShowtimeDAO showtimeDAO = new ShowtimeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                request.getRequestDispatcher("/showtime-form.jsp").forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("editId", editId);
                request.getRequestDispatcher("/showtime-form.jsp").forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                showtimeDAO.deleteShowtime(deleteId);
                response.sendRedirect("showtime");
                break;
                
            default:
                List<Showtime> showtimeList = showtimeDAO.getAllShowtimes();
                request.setAttribute("showtimes", showtimeList);
                request.getRequestDispatcher("/showtime-list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        String movieIdStr = request.getParameter("movieId");
        String roomIdStr = request.getParameter("roomId");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String ticketPriceStr = request.getParameter("ticketPrice");

        Showtime showtime = new Showtime();
        showtime.setMovieId(Integer.parseInt(movieIdStr));
        showtime.setRoomId(Integer.parseInt(roomIdStr));
        showtime.setStartTime(startTime);
        showtime.setEndTime(endTime);
        showtime.setTicketPrice(Double.parseDouble(ticketPriceStr));

        if (idStr == null || idStr.isEmpty()) {
            showtimeDAO.addShowtime(showtime);
        } else {
            showtime.setShowtimeId(Integer.parseInt(idStr));
            showtimeDAO.updateShowtime(showtime);
        }
        response.sendRedirect("showtime");
    }
}