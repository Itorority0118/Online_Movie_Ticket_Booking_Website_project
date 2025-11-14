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
                // Form thêm user (admin)
                request.getRequestDispatcher("/user-form.jsp").forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                User userToEdit = userDAO.getUserByID(editId);
                request.setAttribute("user", userToEdit);
                request.getRequestDispatcher("/user-form.jsp").forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                userDAO.deleteUser(deleteId);
                response.sendRedirect("user");
                break;

            case "logout":
                HttpSession session = request.getSession();
                session.invalidate();
                response.sendRedirect("login.jsp");
                break;

            case "register":
                // Hiển thị form đăng ký
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                break;

            default:
                // Hiển thị danh sách user (admin)
                List<User> userList = userDAO.getAllUsers();
                request.setAttribute("users", userList);
                request.getRequestDispatcher("/user-list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("login".equals(action)) {
            // Xử lý login
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            User user = userDAO.login(email, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", user);
                response.sendRedirect("dashboard.jsp"); // trang chính sau login
            } else {
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            return;
        }

        if ("register".equals(action)) {
            // Xử lý đăng ký user mới
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");

            User newUser = new User();
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setPhone(phone);
            newUser.setRole("user");       // role mặc định
            newUser.setStatus("active");    // status mặc định

            boolean success = userDAO.addUser(newUser);
            if (success) {
                response.sendRedirect("login.jsp"); // đăng ký xong về login
            } else {
                request.setAttribute("error", "Registration failed. Try again!");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
            return;
        }

        // Thêm/Cập nhật user (admin)
        String idStr = request.getParameter("id");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        String status = request.getParameter("status");

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        user.setStatus(status);

        if (idStr == null || idStr.isEmpty()) {
            // Thêm mới user (admin)
            String password = request.getParameter("password");
            user.setPassword(password);
            userDAO.addUser(user);
        } else {
            // Cập nhật user
            user.setUserId(Integer.parseInt(idStr));
            userDAO.updateUser(user);
        }

        response.sendRedirect("user");
    }
}
