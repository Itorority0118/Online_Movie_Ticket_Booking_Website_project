package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DBConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/TestConnectionServlet")	
public class TestConnectionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TestConnectionServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h2>Database Connection Test</h2>");

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT TOP 5 * FROM Movie")) {

            out.println("<p style='color:green;'>✅ Connection Successful!</p>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>ID</th><th>Title</th><th>Genre</th></tr>");
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("MovieId") + "</td>");
                out.println("<td>" + rs.getString("Title") + "</td>");
                out.println("<td>" + rs.getString("Genre") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

        } catch (Exception e) {
            out.println("<p style='color:red;'>❌ Connection Failed: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}