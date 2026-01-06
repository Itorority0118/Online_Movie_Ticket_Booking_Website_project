package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import model.User;
import java.io.IOException;

@WebFilter(urlPatterns = {
	    "/admin",
	    "/admin/*",
	    "/user"
	})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        String action = req.getParameter("action");
        String uri = req.getRequestURI();

     // Cho phép login / register / forgot / updateProfile
        if (action != null && (
                action.equals("login") ||
                action.equals("register") ||
                action.equals("forgot") ||
                action.equals("updateProfile")
        )) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // Chưa login → về login
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Chỉ ADMIN mới được vào các action admin
        if (uri.startsWith(req.getContextPath() + "/admin")) {
            if (!"admin".equalsIgnoreCase(user.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/error/access-denied.jsp");
                return;
            }
        }

        chain.doFilter(request, response);

    }
}