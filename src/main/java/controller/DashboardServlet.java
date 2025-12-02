package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import dao.UserDAO;

/**
 * Servlet implementation class DashboardServlet
 */
@WebServlet("/admin")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    UserDAO userDAO = new UserDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DashboardServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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

        req.getRequestDispatcher("/admin/dashboard.jsp")
           .forward(req, resp);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
