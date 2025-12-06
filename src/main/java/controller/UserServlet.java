package controller;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String action = request.getParameter("action");
        if (action == null) action = "list";

        request.setAttribute("user", null);

        switch (action) {

            case "new":
                request.setAttribute("user", null);
                request.getSession().setAttribute("user", null); 
                if (isAjax)
                    request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=user-form.jsp")
                            .forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                User userToEdit = userDAO.getUserByID(editId);

                request.setAttribute("user", userToEdit);

                if (isAjax)
                    request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=user-form.jsp")
                            .forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                userDAO.deleteUser(deleteId);
                response.sendRedirect(request.getContextPath() + "/user?action=list");
                break;

            case "logout":
                request.getSession().invalidate();
                response.sendRedirect("login.jsp");
                break;
                
            case "register":
                request.getRequestDispatcher("/register.jsp")
                        .forward(request, response);
                break;

            case "forgot":
                request.getRequestDispatcher("/forgot-password.jsp")
                        .forward(request, response);
                break;

            case "list":
            default:
                String role = request.getParameter("role");
                String status = request.getParameter("status");
                String search = request.getParameter("search");

                List<User> userList;

                if (search != null && !search.trim().isEmpty()) {
                    userList = userDAO.searchUsers(search.trim());
                } else {
                    if ((role != null && !role.isEmpty()) && (status != null && !status.isEmpty())) {
                        userList = userDAO.getUsersByRoleAndStatus(role, status);
                    } else if (role != null && !role.isEmpty()) {
                        userList = userDAO.getUsersByRole(role);
                    } else if (status != null && !status.isEmpty()) {
                        userList = userDAO.getUsersByStatus(status);
                    } else {
                        userList = userDAO.getAllUsers();
                    }
                }

                // count users
                int totalUsers = userDAO.countUsers();
                int totalAdmins = userDAO.countUsersByRole("admin");
                int totalActive = userDAO.countUsersByStatus("active");
                int totalInactive = userDAO.countUsersByStatus("inactive");

                request.setAttribute("users", userList);
                request.setAttribute("totalUsers", totalUsers);
                request.setAttribute("totalAdmins", totalAdmins);
                request.setAttribute("totalActive", totalActive);
                request.setAttribute("totalBanned", totalInactive);

                if (isAjax)
                    request.getRequestDispatcher("/admin/user-list.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=user-list.jsp")
                            .forward(request, response);
                break;

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        // ---------------- LOGIN ----------------
        if ("login".equals(action)) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            User user = userDAO.login(email, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                if ("admin".equalsIgnoreCase(user.getRole()))
                    response.sendRedirect(request.getContextPath() + "/admin");
                else
                    response.sendRedirect("index.jsp");
            } else {
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            return;
        }

        // ---------------- REGISTER ----------------
        if ("register".equals(action)) {
            User newUser = new User();
            newUser.setFullName(request.getParameter("fullName"));
            newUser.setEmail(request.getParameter("email"));
            newUser.setPassword(request.getParameter("password"));
            newUser.setPhone(request.getParameter("phone"));
            newUser.setRole("user");
            newUser.setStatus("active");

            boolean success = userDAO.addUser(newUser);

            if (success)
                response.sendRedirect("login.jsp");
            else {
                request.setAttribute("error", "Registration failed. Try again.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
            return;
        }

        // ---------------- FORGOT PASSWORD ----------------
        if ("forgot".equals(action)) {
            String email = request.getParameter("email");

            User user = userDAO.getUserByEmail(email);

            if (user == null) {
                request.setAttribute("error", "Email not found!");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                return;
            }

            String newPass = "pw" + (int) (Math.random() * 9000 + 1000);

            boolean updated = userDAO.updatePassword(email, newPass);

            if (updated)
                request.setAttribute("message", "Your new password is: " + newPass);
            else
                request.setAttribute("error", "Failed to reset password. Try again.");

            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }

        // ---------------- ADD / EDIT ----------------
        String idStr = request.getParameter("id");

        // Important: NEW user only created when adding
        User user;

        if (idStr == null || idStr.isEmpty()) {
            user = new User();

            // check duplicate only for add
            if (userDAO.emailExists(request.getParameter("email"))) {
                request.setAttribute("error", "Email already exists!");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/admin/dashboard.jsp?page=user-form.jsp")
                        .forward(request, response);
                return;
            }

            user.setPassword(request.getParameter("password"));
        } else {
            user = userDAO.getUserByID(Integer.parseInt(idStr));
        }

        user.setUserId(idStr == null || idStr.isEmpty() ? 0 : Integer.parseInt(idStr));
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setRole(request.getParameter("role"));
        user.setStatus(request.getParameter("status"));

        if (idStr == null || idStr.isEmpty()) {
            userDAO.addUser(user);
        } else {
            String newPassword = request.getParameter("password");
            if (newPassword != null && !newPassword.isEmpty())
                user.setPassword(newPassword);
            userDAO.updateUser(user);
        }

        if (!"login".equals(action) &&
        	    !"register".equals(action) &&
        	    !"forgot".equals(action)) {

        	    response.sendRedirect(request.getContextPath() + "/user?action=list");
        }
    }
}
