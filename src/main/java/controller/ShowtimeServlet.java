package controller;

import dao.ShowtimeDAO;
import dao.CinemaDAO; 
import dao.MovieDAO; 
import model.Showtime;
import model.Movie; 
import model.Cinema; 

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
    private CinemaDAO cinemaDAO = new CinemaDAO(); 
    private MovieDAO movieDAO = new MovieDAO(); 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) action = "search";

        // ✅ 1. LUÔN CUNG CẤP DANH SÁCH LỌC CHO JSP (View)
        request.setAttribute("cityList", cinemaDAO.getDistinctCities());
        request.setAttribute("genreList", movieDAO.getDistinctGenres());
        request.setAttribute("ageRatingList", movieDAO.getAgeRatingList());
        
        // Lấy tham số cho bộ lọc
        String cityParam = request.getParameter("city");
        String cinemaIdStr = request.getParameter("cinemaId");
        String dateParam = request.getParameter("date");
        String genreParam = request.getParameter("genre");
        String ageRatingParam = request.getParameter("ageRating");

        // Cập nhật danh sách rạp theo thành phố đã chọn (cần cho select box thứ hai)
        List<Cinema> cinemaList;
        if (cityParam != null && !cityParam.isEmpty()) {
            cinemaList = cinemaDAO.getCinemasByCity(cityParam);
        } else {
            cinemaList = cinemaDAO.getAllCinemas(); 
        }
        request.setAttribute("cinemaList", cinemaList);


        switch (action) {
            case "search":
                // =========================================================
                // ✅ LOGIC CHÍNH: TÌM KIẾM LỊCH CHIẾU CHO KHÁCH HÀNG
                // =========================================================
                
                // Kiểm tra điều kiện tìm kiếm tối thiểu (Rạp và Ngày là bắt buộc)
                boolean isFilteredSearch = (cinemaIdStr != null && !cinemaIdStr.isEmpty() && dateParam != null && !dateParam.isEmpty());

                List<Movie> showtimeMovies = null;

                if (isFilteredSearch) {
                    // ✅ TRƯỜNG HỢP 1: CÓ THAM SỐ LỌC HỢP LỆ (Thực hiện tìm kiếm lịch chiếu cụ thể)
                    try {
                        int cinemaId = Integer.parseInt(cinemaIdStr);
                        
                        // Gọi DAO với các bộ lọc đầy đủ
                        showtimeMovies = 
                                showtimeDAO.getShowtimesByFilter(
                                    cinemaId, 
                                    dateParam, 
                                    genreParam, 
                                    ageRatingParam
                                );
                        
                    } catch (NumberFormatException e) {
                         request.setAttribute("errorMessage", "ID Rạp không hợp lệ.");
                    }
                    
                } else {
                    // ✅ TRƯỜNG HỢP 2: LẦN TRUY CẬP ĐẦU TIÊN / THIẾU THAM SỐ (Hiển thị MẶC ĐỊNH)
                    showtimeMovies = movieDAO.getAllMovies(); 
                    
                    if (showtimeMovies != null && !showtimeMovies.isEmpty()) {
                        // Chỉ set defaultMessage khi có phim để hiển thị
                        request.setAttribute("defaultMessage", "Dưới đây là tất cả các phim đang chiếu. Vui lòng chọn Rạp và Ngày để xem lịch chiếu cụ thể.");
                    } else {
                         request.setAttribute("errorMessage", "Hiện tại không có phim nào đang chiếu.");
                    }
                }
                
                // ✅ FIX: Gán danh sách phim vào request scope CHỈ MỘT LẦN ở cuối logic search
                request.setAttribute("showtimeMovies", showtimeMovies);
                
                // Chuyển tiếp đến trang hiển thị lịch chiếu (showtimes.jsp)
                request.getRequestDispatcher("/showtimes.jsp").forward(request, response);
                break;
                
            // =========================================================
            // ✅ LOGIC CRUD: QUẢN LÝ SUẤT CHIẾU (Dành cho Admin)
            // =========================================================
            
            case "new":
                // Display form to add a new showtime
                request.getRequestDispatcher("/showtime-form.jsp").forward(request, response);
                break;

            case "edit":
                // Display form to edit an existing showtime
                int editId = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("editId", editId);
                // (Cần thêm logic lấy đối tượng Showtime bằng ID)
                request.getRequestDispatcher("/showtime-form.jsp").forward(request, response);
                break;

            case "delete":
                // Delete a showtime by ID
                int deleteId = Integer.parseInt(request.getParameter("id"));
                showtimeDAO.deleteShowtime(deleteId);
                response.sendRedirect("showtime?action=list"); // Chuyển về danh sách admin
                break;
                
            case "list":
            default:
                // Display list of all showtimes (Admin view)
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
        try {
            showtime.setMovieId(Integer.parseInt(movieIdStr));
            showtime.setRoomId(Integer.parseInt(roomIdStr));
            showtime.setStartTime(startTime);
            showtime.setEndTime(endTime);
            showtime.setTicketPrice(Double.parseDouble(ticketPriceStr));
        } catch (NumberFormatException e) {
             // Xử lý lỗi input không hợp lệ
             System.err.println("Lỗi chuyển đổi số trong doPost: " + e.getMessage());
             response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu nhập không hợp lệ.");
             return;
        }


        if (idStr == null || idStr.isEmpty()) {
            // Add new showtime
            showtimeDAO.addShowtime(showtime);
        } else {
            // Update existing showtime
            showtime.setShowtimeId(Integer.parseInt(idStr));
            showtimeDAO.updateShowtime(showtime);
        }

        response.sendRedirect("showtime?action=list"); // Chuyển về danh sách admin
    }
}