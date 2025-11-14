package controller;

import dao.CinemaDAO;
import model.Cinema;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet("/cinema")
public class CinemaServlet extends HttpServlet {

    private CinemaDAO cinemaDAO = new CinemaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                request.getRequestDispatcher("/cinema-form.jsp").forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Cinema cinemaToEdit = cinemaDAO.getCinemaById(editId);
                request.setAttribute("cinema", cinemaToEdit);
                request.getRequestDispatcher("/cinema-form.jsp").forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                cinemaDAO.deleteCinema(deleteId);
                response.sendRedirect("cinema");
                break;

            default:
                List<Cinema> cinemaList = cinemaDAO.getAllCinemas();
                request.setAttribute("cinemas", cinemaList);
                request.getRequestDispatcher("/cinema-list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
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

        response.sendRedirect("cinema");
    }
}
