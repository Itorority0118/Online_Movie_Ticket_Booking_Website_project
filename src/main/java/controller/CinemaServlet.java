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

            case "new":
                request.setAttribute("cinema", null);

                if (isAjax)
                    request.getRequestDispatcher("/admin/cinema-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=cinema-form.jsp")
                           .forward(request, response);
                break;

            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                Cinema cinema = cinemaDAO.getCinemaById(id);
                request.setAttribute("cinema", cinema);

                if (isAjax)
                    request.getRequestDispatcher("/admin/cinema-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=cinema-form.jsp")
                           .forward(request, response);
                break;

            case "list":
                String keyword = request.getParameter("keyword");
                String city = request.getParameter("city");

                List<Cinema> cinemas = cinemaDAO.searchCinemas(keyword, city);
                request.setAttribute("cinemas", cinemas);
                request.setAttribute("cityList", getCityList());

                if (isAjax)
                    request.getRequestDispatcher("/admin/cinema-list.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=cinema-list.jsp").forward(request, response);
                break;
                
            default:
                List<Cinema> cinemaList = cinemaDAO.getAllCinemas();
                request.setAttribute("cinemas", cinemaList);
                request.setAttribute("cityList", getCityList());

                if (isAjax)
                    request.getRequestDispatcher("/admin/cinema-list.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=cinema-list.jsp").forward(request, response);
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
            boolean success = cinemaDAO.deleteCinema(id);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": " + success + "}");
            out.flush();
            return;
        }

        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String phone = request.getParameter("phone");

        Cinema cinema = new Cinema();
        cinema.setName(name);
        cinema.setAddress(address);
        cinema.setCity(city);
        cinema.setPhone(phone);

        if (idStr == null || idStr.isEmpty()) {
            cinemaDAO.addCinema(cinema);
        } else {
            cinema.setCinemaId(Integer.parseInt(idStr));
            cinemaDAO.updateCinema(cinema);
        }

        response.sendRedirect(request.getContextPath() + "/cinema?action=list");
    }
}