package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import dao.MovieDAO;
import dao.UserDAO;

@WebServlet("/admin")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    UserDAO userDAO = new UserDAO();
    MovieDAO movieDAO = new MovieDAO();
       
    public DashboardServlet() {
        super();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int totalUsers = userDAO.countUsers();
        int totalAdmins = userDAO.countUsersByRole("admin");
        int totalActive = userDAO.countUsersByStatus("active");
        int totalInactive = userDAO.countUsersByStatus("inactive");

        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("totalAdmins", totalAdmins);
        req.setAttribute("totalActive", totalActive);
        req.setAttribute("totalInactive", totalInactive);
        
        int totalMovies = movieDAO.countMovies();
        req.setAttribute("totalMovies", totalMovies);

        req.getRequestDispatcher("/admin/dashboard.jsp")
           .forward(req, resp);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}