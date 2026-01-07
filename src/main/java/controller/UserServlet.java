package controller;

import dao.UserDAO;
import model.User;
import utils.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String action = request.getParameter("action");
        if (action == null) action = "list";
        switch (action) {

	        case "new":
	            request.setAttribute("formUser", new User());
	            if (isAjax)
	                request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
	            else
	                request.getRequestDispatcher("/admin/dashboard.jsp?page=user-form.jsp")
	                        .forward(request, response);
	            break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                User userToEdit = userDAO.getUserByID(editId);
                request.setAttribute("formUser", userToEdit);
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
                response.sendRedirect(request.getContextPath() + "/login.jsp");
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

        if ("login".equals(action)) {

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            Map<String, String> errors = new HashMap<>();

            if (email == null || email.trim().isEmpty()) {
                errors.put("email", "Email is required");
            } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.put("email", "Invalid email format");
            }

            if (password == null || password.trim().isEmpty()) {
                errors.put("password", "Password is required");
            }

            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            User user = userDAO.login(email.trim(), password);

            if (user == null) {
                errors.put("general", "Invalid email or password");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            session.setAttribute(
                "role",
                "admin".equalsIgnoreCase(user.getRole()) ? "ADMIN" : "CUSTOMER"
            );

            if ("admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/movie?action=now_showing");
            }
            return;
        }

        // ---------------- REGISTER ----------------
        if ("register".equals(action)) {

            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");

            Map<String, String> errors = new HashMap<>();

            if (fullName == null || fullName.trim().length() < 3) {
                errors.put("fullName", "Full name must be at least 3 characters");
            }

            if (email == null || email.trim().isEmpty()) {
                errors.put("email", "Email is required");
            } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.put("email", "Invalid email format");
            } else if (userDAO.emailExists(email)) {
                errors.put("email", "Email already exists");
            }

            if (password == null || password.length() < 6) {
                errors.put("password", "Password must be at least 6 characters");
            }

            if (phone != null && !phone.trim().isEmpty()) {
                if (!phone.matches("\\d{9,11}")) {
                    errors.put("phone", "Phone must be 9–11 digits");
                }
            }

            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            User newUser = new User();
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setPhone(phone);
            newUser.setRole("user");
            newUser.setStatus("active");

            userDAO.addUser(newUser);
            response.sendRedirect("login.jsp");
            return;
        }

        // ---------------- FORGOT PASSWORD ----------------
        if ("forgot".equals(action)) {

            String email = request.getParameter("email");

            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("error", "Email is required");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                request.setAttribute("error", "Invalid email format");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                return;
            }

            User user = userDAO.getUserByEmail(email);

            if (user == null) {
                request.setAttribute("error", "Email not found!");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                return;
            }

            String newPass = "pw" + (int) (Math.random() * 900000 + 100000);
            userDAO.updatePassword(email, newPass);
            try {
                EmailUtil.sendResetPassword(email, newPass);
                request.setAttribute(
                    "message",
                    "A new password has been sent to your email."
                );
            } catch (javax.mail.MessagingException e) {
                e.printStackTrace();
                request.setAttribute(
                    "error",
                    "Failed to send reset email. Please try again later."
                );
            }

            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }
        // ---------------- UPDATE PROFILE (CUSTOMER) ----------------
        if ("updateProfile".equals(action)) {

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            if (currentUser != null) {
                currentUser.setFullName(request.getParameter("fullName"));
                currentUser.setPhone(request.getParameter("phone"));

                userDAO.updateUser(currentUser);

                // cập nhật lại session
                session.setAttribute("user", currentUser);
            }

            response.sendRedirect(request.getContextPath() + "/movie?action=now_showing");
            return;
        }


        // ---------------- ADD / EDIT ----------------
        String idStr = request.getParameter("id");
        boolean isAdd = (idStr == null || idStr.isEmpty());

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        String status = request.getParameter("status");
        String password = request.getParameter("password");

        Map<String, String> errors = new HashMap<>();

        if (fullName == null || fullName.trim().length() < 3) {
            errors.put("fullName", "Full name must be at least 3 characters");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email is required");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.put("email", "Invalid email format");
        } else {
            if (isAdd) {
                if (userDAO.emailExists(email)) {
                    errors.put("email", "Email already exists");
                }
            } else {
                User existingUser = userDAO.getUserByEmail(email);
                if (existingUser != null && existingUser.getUserId() != Integer.parseInt(idStr)) {
                    errors.put("email", "Email already exists");
                }
            }
        }

        if (phone != null && !phone.trim().isEmpty()) {
            if (!phone.matches("\\d{9,11}")) {
                errors.put("phone", "Phone must be 9–11 digits");
            }
        }
        
        if (isAdd) {
            if (password == null || password.length() < 6) {
                errors.put("password", "Password must be at least 6 characters");
            }
        } else {
            if (password != null && !password.isEmpty() && password.length() < 6) {
                errors.put("password", "New password must be at least 6 characters");
            }
        }

        if (!errors.isEmpty()) {
            User formUser = new User();

            if (isAdd) {
                formUser = new User();
            } else {
                formUser = new User();
                formUser.setUserId(Integer.parseInt(idStr));
            }

            formUser.setFullName(fullName);
            formUser.setEmail(email);
            formUser.setPhone(phone);
            formUser.setRole(role);
            formUser.setStatus(status);

            request.setAttribute("errors", errors);
            request.setAttribute("formUser", formUser);
            request.getRequestDispatcher("/admin/dashboard.jsp?page=user-form.jsp")
                    .forward(request, response);
            return;
        }

        User user;

        if (isAdd) {
            user = new User();
            user.setPassword(password);
        } else {
            user = userDAO.getUserByID(Integer.parseInt(idStr));
            if (password != null && !password.isEmpty()) {
                user.setPassword(password);
            }
        }

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        user.setStatus(status);

        if (isAdd) {
            userDAO.addUser(user);
        } else {
            userDAO.updateUser(user);
        }
        response.sendRedirect(request.getContextPath() + "/user?action=list");
    }
}