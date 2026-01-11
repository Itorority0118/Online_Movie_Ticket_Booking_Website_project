package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet("/member")
public class MemberServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // 1️⃣ CHƯA ĐĂNG NHẬP → LOGIN
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // 2️⃣ LẤY USER
        User user = (User) session.getAttribute("user");

        // 3️⃣ ADMIN → CẤM
        if ("ADMIN".equals(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, 
                "Admin không được truy cập trang thành viên");
            return;
        }

        // 4️⃣ USER → CHO VÀO
        req.getRequestDispatcher("/member.jsp")
        .forward(req, resp);

    }
}
