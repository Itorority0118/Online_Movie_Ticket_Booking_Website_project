package controller;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {

            case "new":
                request.getRequestDispatcher("/WEB-INF/views/user-form.jsp").forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                User userToEdit = userDAO.getUserByID(editId);
                request.setAttribute("user", userToEdit);
                request.getRequestDispatcher("/WEB-INF/views/user-form.jsp").forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                userDAO.deleteUser(deleteId);
                response.sendRedirect("user");
                break;

            case "logout":
                request.getSession().invalidate();
                response.sendRedirect("login.jsp");
                break;

            case "register":
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                break;
                
            case "forgot":
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                break;

            default:
                List<User> userList = userDAO.getAllUsers();
                request.setAttribute("users", userList);
                request.getRequestDispatcher("/WEB-INF/views/user-list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("login".equals(action)) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            User user = userDAO.login(email, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                if ("admin".equalsIgnoreCase(user.getRole())) {
                	request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
                } else {
                    response.sendRedirect("index.jsp");
                }

            } else {
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            return;
        }

        if ("register".equals(action)) {

            User newUser = new User();
            newUser.setFullName(request.getParameter("fullName"));
            newUser.setEmail(request.getParameter("email"));
            newUser.setPassword(request.getParameter("password"));
            newUser.setPhone(request.getParameter("phone"));
            newUser.setRole("user");
            newUser.setStatus("active");

            boolean success = userDAO.addUser(newUser);

            if (success) {
                response.sendRedirect("login.jsp");
            } else {
                request.setAttribute("error", "Registration failed. Try again.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
            return;
        }
        
        if ("forgot".equals(action)) {
            String email = request.getParameter("email");

            User user = userDAO.login(email, "");
            user = userDAO.getUserByEmail(email);

            if (user == null) {
                request.setAttribute("error", "Email not found!");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                return;
            }

            String newPass = "pw" + (int)(Math.random() * 9000 + 1000);

            boolean updated = userDAO.updatePassword(email, newPass);

            if (updated) {
                request.setAttribute("message", "Your new password is: " + newPass);
            } else {
                request.setAttribute("error", "Failed to reset password. Try again.");
            }

            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }


        String idStr = request.getParameter("id");

        User user = new User();
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setRole(request.getParameter("role"));
        user.setStatus(request.getParameter("status"));

        if (idStr == null || idStr.isEmpty()) {
            user.setPassword(request.getParameter("password"));
            userDAO.addUser(user);
        } else {
            user.setUserId(Integer.parseInt(idStr));
            userDAO.updateUser(user);
        }

        response.sendRedirect("user");
    }
}