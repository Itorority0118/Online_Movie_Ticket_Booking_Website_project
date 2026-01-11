package controller;

import dao.ShowtimeDAO;

import dao.CinemaDAO;
import dao.MovieDAO;
import dao.RoomDAO;
import model.Showtime;
import model.Movie;
import model.Cinema;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
@WebServlet("/showtime")
public class ShowtimeServlet extends HttpServlet {

    private ShowtimeDAO showtimeDAO = new ShowtimeDAO();
    private MovieDAO movieDAO = new MovieDAO();
    private CinemaDAO cinemaDAO = new CinemaDAO();
    private RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) action = "search";

        String role = request.getParameter("role");
        boolean isAdmin = "admin".equals(role);

        request.setAttribute("cityList", cinemaDAO.getDistinctCities());
        request.setAttribute("genreList", movieDAO.getDistinctGenres());
        //request.setAttribute("ageRatingList", movieDAO.getAgeRatingList());

        String cityParam = request.getParameter("city");
        String cinemaIdStr = request.getParameter("cinemaId");
        String dateParam = request.getParameter("date");
        String genreParam = request.getParameter("genre");
        String ageRatingParam = request.getParameter("ageRating");
        
     // ===== API CHO MODAL (USER) =====
        if ("byMovieCinema".equals(action)) {
            int movieId = Integer.parseInt(request.getParameter("movieId"));
            int cinemaId = Integer.parseInt(request.getParameter("cinemaId"));

            List<Showtime> list =
                showtimeDAO.getShowtimesByMovieAndCinema(movieId, cinemaId);

            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();

            out.print("[");
            for (int i = 0; i < list.size(); i++) {
                Showtime s = list.get(i);
                out.print("{");
                out.print("\"showtimeId\":" + s.getShowtimeId() + ",");
                out.print("\"startTime\":\"" + s.getStartTime() + "\",");
                out.print("\"ticketPrice\":" + s.getTicketPrice());
                out.print("}");
                if (i < list.size() - 1) out.print(",");
            }
            out.print("]");
            out.flush();
            return;
        }


        if (!isAdmin) {
        	if (!isAdmin && "byMovieCinema".equals(action)) {
        	    int movieId = Integer.parseInt(request.getParameter("movieId"));
        	    int cinemaId = Integer.parseInt(request.getParameter("cinemaId"));

        	    List<Showtime> list =
        	        showtimeDAO.getShowtimesByMovieAndCinema(movieId, cinemaId);

        	    response.setContentType("application/json");
        	    response.setCharacterEncoding("UTF-8");
        	    response.setContentType("application/json");
        	    response.setCharacterEncoding("UTF-8");

        	    PrintWriter out = response.getWriter();
        	    out.print("[");

        	    for (int i = 0; i < list.size(); i++) {
        	        Showtime s = list.get(i);
        	        out.print("{");
        	        out.print("\"showtimeId\":" + s.getShowtimeId() + ",");
        	        out.print("\"startTime\":\"" + s.getStartTime() + "\",");
        	        out.print("\"endTime\":\"" + s.getEndTime() + "\",");
        	        out.print("\"ticketPrice\":" + s.getTicketPrice());
        	        out.print("}");
        	        if (i < list.size() - 1) out.print(",");
        	    }

        	    out.print("]");
        	    out.flush();
        	    return;
        	}

            // =========================
            // LUỒNG USER
            // =========================
            List<Cinema> cinemaList;
            if (cityParam != null && !cityParam.isEmpty()) {
                cinemaList = cinemaDAO.getCinemasByCity(cityParam);
            } else {
                cinemaList = cinemaDAO.getAllCinemas();
            }
            request.setAttribute("cinemaList", cinemaList);

            switch (action) {
                case "search":
                    boolean isFiltered = (cinemaIdStr != null && !cinemaIdStr.isEmpty() && dateParam != null && !dateParam.isEmpty());
                    List<Movie> showtimeMovies = null;
                    if (isFiltered) {
                        try {
                            int cinemaId = Integer.parseInt(cinemaIdStr);
                            showtimeMovies = showtimeDAO.getShowtimesByFilter(cinemaId, dateParam, genreParam, ageRatingParam);
                        } catch (NumberFormatException e) {
                            request.setAttribute("errorMessage", "ID rạp không hợp lệ.");
                        }
                    } else {
                        showtimeMovies = movieDAO.getAllMovies();
                        if (showtimeMovies != null && !showtimeMovies.isEmpty()) {
                            request.setAttribute("defaultMessage", "Dưới đây là tất cả phim đang chiếu. Chọn rạp và ngày để xem lịch chiếu cụ thể.");
                        } else {
                            request.setAttribute("errorMessage", "Hiện tại không có phim nào đang chiếu.");
                        }
                    }
                    request.setAttribute("showtimeMovies", showtimeMovies);
                    request.getRequestDispatcher("/showtimes.jsp").forward(request, response);
                    break;

                case "listByRoom":
                    String roomIdStr = request.getParameter("roomId");
                    if (roomIdStr != null && !roomIdStr.isEmpty()) {
                        try {
                            int roomId = Integer.parseInt(roomIdStr);
                            List<Showtime> roomShowtimes = showtimeDAO.getShowtimesByRoom(roomId);
                            request.setAttribute("showtimes", roomShowtimes);
                            request.setAttribute("roomId", roomId);
                            request.getRequestDispatcher("/showtime-list.jsp").forward(request, response);
                        } catch (NumberFormatException e) {
                            request.setAttribute("errorMessage", "ID phòng không hợp lệ.");
                            response.sendRedirect("cinema?action=list");
                        }
                    } else {
                        response.sendRedirect("cinema?action=list");
                    }
                    break;

                default:
                    response.sendRedirect("showtime?action=search");
            }

        } else {
            // =========================
            // LUỒNG ADMIN
            // =========================
            request.setAttribute("activeSidebar", "showtime");

            switch (action) {
            case "list": {
                String movieName = request.getParameter("movieName");
                String startTime = request.getParameter("startTime");
                String endTime = request.getParameter("endTime");
                String roomIdStr = request.getParameter("roomId");

                List<Showtime> showtimes;

                if (roomIdStr != null && !roomIdStr.isEmpty()) {
                    int roomId = Integer.parseInt(roomIdStr);
                    showtimes = showtimeDAO.searchByAdminInRoom(
                            roomId, movieName, startTime, endTime
                    );
                    request.setAttribute("roomId", roomId);
                } else {
                    showtimes = showtimeDAO.searchByAdmin(
                            movieName, startTime, endTime
                    );
                }

                request.setAttribute("showtimes", showtimes);

                Map<Integer, String> movieMap = new HashMap<>();
                for (Showtime s : showtimes) {
                    if (!movieMap.containsKey(s.getMovieId())) {
                        Movie m = movieDAO.getMovieById(s.getMovieId());
                        if (m != null) movieMap.put(s.getMovieId(), m.getTitle());
                    }
                }
                request.setAttribute("movieMap", movieMap);

                request.getRequestDispatcher(
                    "/admin/dashboard.jsp?page=showtime-list.jsp"
                ).forward(request, response);
                break;
            }

            case "listByRoom":
                String roomIdStr = request.getParameter("roomId");
                if (roomIdStr != null && !roomIdStr.isEmpty()) {
                    try {
                        int roomId = Integer.parseInt(roomIdStr);
                        List<Showtime> roomShowtimes = showtimeDAO.getShowtimesByRoom(roomId);
                        request.setAttribute("showtimes", roomShowtimes);
                        request.setAttribute("roomId", roomId);
                        int cinemaId = roomDAO.getCinemaIdByRoom(roomId);
                        request.setAttribute("cinemaId", cinemaId);

                        Map<Integer, String> roomMovieMap = new HashMap<>();
                        for (Showtime s : roomShowtimes) {
                            if (!roomMovieMap.containsKey(s.getMovieId())) {
                                Movie m = movieDAO.getMovieById(s.getMovieId());
                                if (m != null) roomMovieMap.put(s.getMovieId(), m.getTitle());
                            }
                        }
                        request.setAttribute("movieMap", roomMovieMap);

                    } catch (NumberFormatException e) {
                        request.setAttribute("errorMessage", "ID phòng không hợp lệ.");
                    }
                } else {
                    request.setAttribute("errorMessage", "Chưa chọn phòng.");
                }

                request.getRequestDispatcher("/admin/dashboard.jsp?page=showtime-list.jsp").forward(request, response);
                break;


                case "new":
                    String roomIdParam = request.getParameter("roomId");
                    if(roomIdParam != null && !roomIdParam.isEmpty()) {
                        request.setAttribute("roomId", Integer.parseInt(roomIdParam));
                    }
                    request.setAttribute("movieList", movieDAO.getAllMovies());
                    request.setAttribute("roomList", roomDAO.getAllRooms());
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=showtime-form.jsp").forward(request, response);
                    break;

                case "edit":
                    int editId = Integer.parseInt(request.getParameter("id"));
                    Showtime showtime = showtimeDAO.getShowtimeById(editId);
                    request.setAttribute("showtime", showtime);
                    request.setAttribute("movieList", movieDAO.getAllMovies());
                    request.setAttribute("roomList", roomDAO.getAllRooms());

                    if (showtime != null) {
                        request.setAttribute("roomId", showtime.getRoomId());
                    }

                    request.getRequestDispatcher("/admin/dashboard.jsp?page=showtime-form.jsp").forward(request, response);
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/showtime?role=admin&action=list");
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String role = request.getParameter("role");
        boolean isAdmin = "admin".equals(role);

        if (!isAdmin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ admin mới được thao tác dữ liệu.");
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean deleted = showtimeDAO.deleteShowtime(id);

            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":" + deleted + "}");
            } else {
                response.sendRedirect(request.getContextPath() + "/showtime?role=admin&action=list");
            }
            return;
        }

        String idStr = request.getParameter("id");
        String movieIdStr = request.getParameter("movieId");
        String roomIdStr = request.getParameter("roomId");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String ticketPriceStr = request.getParameter("ticketPrice");

        // Validate
        Map<String, String> errors = new HashMap<>();

        if (movieIdStr == null || movieIdStr.isEmpty()) {
            errors.put("movieId", "Movie chưa điền");
        }
        if (roomIdStr == null || roomIdStr.isEmpty()) {
            errors.put("roomId", "Room chưa điền");
        }
        if (startTime == null || startTime.isEmpty()) {
            errors.put("startTime", "Start Time chưa chọn");
        }
        if (endTime == null || endTime.isEmpty()) {
            errors.put("endTime", "End Time chưa chọn");
        }
        if (ticketPriceStr == null || ticketPriceStr.isEmpty()) {
            errors.put("ticketPrice", "Ticket Price chưa điền");
        }

        Showtime showtime = new Showtime();
        try {
            if (movieIdStr != null && !movieIdStr.isEmpty()) showtime.setMovieId(Integer.parseInt(movieIdStr));
            if (roomIdStr != null && !roomIdStr.isEmpty()) showtime.setRoomId(Integer.parseInt(roomIdStr));
            if (startTime != null) showtime.setStartTime(startTime.replace("T", " "));
            if (endTime != null) showtime.setEndTime(endTime.replace("T", " "));
            if (ticketPriceStr != null && !ticketPriceStr.isEmpty()) {
                try {
                    int price = Integer.parseInt(ticketPriceStr);

                    if (price < 1000) {
                        errors.put("ticketPrice", "Ticket price phải ít nhất 1,000 VND");
                    } else if (price % 1000 != 0) {
                        errors.put("ticketPrice", "Ticket price phải là bội số của 1,000 VND");
                    } else {
                        showtime.setTicketPrice(price);
                    }

                } catch (NumberFormatException e) {
                    errors.put("ticketPrice", "Invalid ticket price format");
                }
            }
            if (startTime != null && endTime != null && !startTime.isEmpty() && !endTime.isEmpty()) {
                if (startTime.compareTo(endTime) >= 0) {
                    errors.put("endTime", "End Time phải sau Start Time");
                }
            }
        } catch (NumberFormatException e) {
            errors.put("ticketPrice", "Sai định dạng");
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("showtime", showtime);
            request.setAttribute("movieList", movieDAO.getAllMovies());
            request.setAttribute("roomList", roomDAO.getAllRooms());
            request.setAttribute("roomId", roomIdStr);

            request.getRequestDispatcher("/admin/dashboard.jsp?page=showtime-form.jsp")
                   .forward(request, response);
            return;
        }

        boolean success;
        if (idStr == null || idStr.isEmpty()) {
            success = showtimeDAO.addShowtime(showtime);
        } else {
            showtime.setShowtimeId(Integer.parseInt(idStr));
            success = showtimeDAO.updateShowtime(showtime);
        }

        if (!success) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể lưu showtime.");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/showtime?role=admin&action=listByRoom&roomId=" + roomIdStr);
    }
}