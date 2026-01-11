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
                try {
                    String errorMsg = userDAO.deleteUser(deleteId);

                    if (isAjax) {
                        response.setContentType("application/json;charset=UTF-8");
                        Map<String, Object> json = new HashMap<>();
                        if (errorMsg != null) {
                            json.put("success", false);
                            json.put("message", errorMsg);
                        } else {
                            json.put("success", true);
                        }
                        new com.google.gson.Gson().toJson(json, response.getWriter());
                        return;
                    } else {
                        if (errorMsg != null) {
                            request.setAttribute("error", errorMsg);
                        }
                        response.sendRedirect(request.getContextPath() + "/user?action=list");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (isAjax) {
                        response.setContentType("application/json;charset=UTF-8");
                        Map<String, Object> json = new HashMap<>();
                        json.put("success", false);
                        json.put("message", "Lỗi server: " + e.getMessage());
                        new com.google.gson.Gson().toJson(json, response.getWriter());
                        return;
                    } else {
                        request.setAttribute("error", "Lỗi server: " + e.getMessage());
                        response.sendRedirect(request.getContextPath() + "/user?action=list");
                        return;
                    }
                }



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
                errors.put("email", "Email chưa điền");
            } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.put("email", "Sai định dạng email");
            }

            if (password == null || password.trim().isEmpty()) {
                errors.put("password", "Password chưa điền");
            }

            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            User user = userDAO.login(email.trim(), password);

            if (user == null) {
                errors.put("general", "Sai email hoặc password");
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
                errors.put("fullName", "Full name phải ít nhất 3 kí tự");
            }

            if (email == null || email.trim().isEmpty()) {
                errors.put("email", "Email chưa điền");
            } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.put("email", "Sai định dạng email");
            } else if (userDAO.emailExists(email)) {
                errors.put("email", "Email đã tồn tại");
            }

            if (password == null || password.length() < 6) {
                errors.put("password", "Password phải ít nhất 6 kí tự");
            }

            if (phone != null && !phone.trim().isEmpty()) {
                if (!phone.matches("\\d{9,11}")) {
                    errors.put("phone", "Số điện thoại phải từ 9-11 số");
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
                request.setAttribute("error", "Email chưa điền");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                request.setAttribute("error", "Sai định dạng email");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                return;
            }

            User user = userDAO.getUserByEmail(email);

            if (user == null) {
                request.setAttribute("error", "Email không tìm thấy!");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                return;
            }

            String newPass = "pw" + (int) (Math.random() * 900000 + 100000);
            userDAO.updatePassword(email, newPass);
            try {
                EmailUtil.sendResetPassword(email, newPass);
                request.setAttribute(
                    "message",
                    "Mật khẩu reset đã được gửi tới email của bạn."
                );
            } catch (javax.mail.MessagingException e) {
                e.printStackTrace();
                request.setAttribute(
                    "error",
                    "Gửi mật khẩu reset thất bại. Vui lòng thử lại sau"
                );
            }

            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }
        
        if ("updateProfile".equals(action)) {
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> jsonResponse = new HashMap<>();

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            if (currentUser == null) {
                jsonResponse.put("success", false);
                Map<String, String> err = new HashMap<>();
                err.put("general", "Bạn cần đăng nhập trước khi cập nhật.");
                jsonResponse.put("errors", err);
            } else {
                String fullName = request.getParameter("fullName");
                String phone = request.getParameter("phone");
                String newPassword = request.getParameter("password");

                Map<String, String> errors = new HashMap<>();

                // Validate fullName chỉ khi người dùng nhập
                if (fullName != null && !fullName.trim().isEmpty()) {
                    fullName = fullName.trim();
                    if (fullName.length() < 3) {
                        errors.put("fullName", "Họ và tên phải ít nhất 3 ký tự");
                    }
                } else {
                    // Nếu không nhập, giữ nguyên fullName hiện tại
                    fullName = currentUser.getFullName();
                }

                // Validate phone nếu nhập
                if (phone != null && !phone.trim().isEmpty()) {
                    if (!phone.matches("\\d{9,11}")) {
                        errors.put("phone", "Số điện thoại phải từ 9-11 chữ số");
                    }
                } else {
                    phone = currentUser.getPhone();
                }

                // Validate password nếu nhập
                if (newPassword != null && !newPassword.trim().isEmpty() && newPassword.length() < 6) {
                    errors.put("password", "Mật khẩu mới phải ít nhất 6 ký tự");
                }

                if (!errors.isEmpty()) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("errors", errors);
                } else {
                    // Update thông tin
                    currentUser.setFullName(fullName);
                    currentUser.setPhone(phone);
                    if (newPassword != null && !newPassword.trim().isEmpty()) {
                        currentUser.setPassword(newPassword);
                    }

                    userDAO.updateUser(currentUser);
                    session.setAttribute("user", currentUser);

                    // Trả về JSON
                    Map<String,Object> userMap = new HashMap<>();
                    userMap.put("userId", currentUser.getUserId());
                    userMap.put("fullName", currentUser.getFullName());
                    userMap.put("email", currentUser.getEmail());
                    userMap.put("phone", currentUser.getPhone());
                    userMap.put("role", currentUser.getRole());
                    userMap.put("status", currentUser.getStatus());
                    userMap.put("createdAt", currentUser.getCreatedAt());

                    jsonResponse.put("success", true);
                    jsonResponse.put("user", userMap);
                }
            }

            String json = new com.google.gson.Gson().toJson(jsonResponse);
            response.getWriter().write(json);
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
            errors.put("fullName", "Họ và tên phải ít nhất 3 kí tự");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email chưa điền");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.put("email", "Sai định dạng email");
        } else {
            if (isAdd) {
                if (userDAO.emailExists(email)) {
                    errors.put("email", "Email đã tồn tại");
                }
            } else {
                User existingUser = userDAO.getUserByEmail(email);
                if (existingUser != null && existingUser.getUserId() != Integer.parseInt(idStr)) {
                    errors.put("email", "Email đã tồn tại");
                }
            }
        }

        if (phone != null && !phone.trim().isEmpty()) {
            if (!phone.matches("\\d{9,11}")) {
                errors.put("phone", "Số điện thoại phải từ 9-11 chữ số");
            }
        }
        
        if (isAdd) {
            if (password == null || password.length() < 6) {
                errors.put("password", "Password phải ít nhất 6 kí tự");
            }
        } else {
            if (password != null && !password.isEmpty() && password.length() < 6) {
                errors.put("password", "New password phải ít nhất 6 kí tự");
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