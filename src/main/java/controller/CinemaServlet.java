package controller;

import dao.CinemaDAO;
import model.Cinema;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

@WebServlet("/cinema")
public class CinemaServlet extends HttpServlet {

    private final CinemaDAO cinemaDAO = new CinemaDAO();

    private List<String> getCityList() {
        return Arrays.asList(
            "Hà Nội","Hồ Chí Minh","Đà Nẵng","Hải Phòng","Cần Thơ",
            "Đắk Lắk","Khánh Hòa","Quảng Ninh","Bình Dương","Đồng Nai",
            "Bình Định","Thừa Thiên Huế","Nghệ An","Thanh Hóa","An Giang",
            "Tiền Giang","Bến Tre","Vũng Tàu","Long An","Lâm Đồng",
            "Hải Dương","Phú Yên","Kon Tum","Quảng Nam","Quảng Ngãi"
        );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String action = request.getParameter("action");
        if (action == null) action = "list";

        request.setAttribute("activeSidebar", "cinema");

        switch (action) {

		        case "byMovie": {
		            int movieId = Integer.parseInt(request.getParameter("movieId"));
		            List<Cinema> cinemas = cinemaDAO.getCinemasByMovie(movieId);
		
		            response.setContentType("application/json;charset=UTF-8");
		            PrintWriter out = response.getWriter();
		
		            out.print("[");
		            for (int i = 0; i < cinemas.size(); i++) {
		                Cinema c = cinemas.get(i);
		                out.print("{");
		                out.print("\"cinemaId\":" + c.getCinemaId() + ",");
		                out.print("\"name\":\"" + c.getName() + "\"");
		                out.print("}");
		                if (i < cinemas.size() - 1) out.print(",");
		            }
		            out.print("]");
		            out.flush();
		            return;
		        }
		        
		        case "citiesByMovie": {
		            int movieId = Integer.parseInt(request.getParameter("movieId"));

		            List<String> cities = cinemaDAO.getCitiesByMovie(movieId);

		            response.setContentType("application/json");
		            response.setCharacterEncoding("UTF-8");
		            response.getWriter().print(new Gson().toJson(cities));
		            break;
		        }

		        case "byMovieCity": {
		            int movieId = Integer.parseInt(request.getParameter("movieId"));
		            String city = request.getParameter("city");

		            List<Cinema> cinemas =
		                cinemaDAO.getCinemasByMovieAndCity(movieId, city);

		            response.setContentType("application/json");
		            response.setCharacterEncoding("UTF-8");
		            response.getWriter().print(new Gson().toJson(cinemas));
		            break;
		        }



            case "new":
                request.setAttribute("cinema", null);
                if (isAjax)
                    request.getRequestDispatcher("/admin/cinema-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=cinema-form.jsp").forward(request, response);
                break;

            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                Cinema cinema = cinemaDAO.getCinemaById(id);
                request.setAttribute("cinema", cinema);
                if (isAjax)
                    request.getRequestDispatcher("/admin/cinema-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=cinema-form.jsp").forward(request, response);
                break;

            case "list":
            default:
                List<Cinema> cinemaList = cinemaDAO.getAllCinemas();
                request.setAttribute("cinemas", cinemaList);
                request.setAttribute("cityList", getCityList());
                if (isAjax)
                    request.getRequestDispatcher("/admin/cinema-table.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=cinema-list.jsp").forward(request, response);
                break;
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}
