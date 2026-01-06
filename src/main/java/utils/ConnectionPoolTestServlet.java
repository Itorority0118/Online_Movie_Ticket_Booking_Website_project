package utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/test-pool")
public class ConnectionPoolTestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        List<Connection> connections = new ArrayList<>();
        int totalConnectionsToTest = 15;

        out.println("=== Demo Connection Pool ===");

        for (int i = 1; i <= totalConnectionsToTest; i++) {
            try {
                Connection conn = DBConnection.getConnection();
                out.println("Got connection " + i + ": " + conn);
                connections.add(conn);
            } catch (SQLException e) {
                out.println("Failed to get connection " + i + ": " + e.getMessage());
            }
        }

        out.println("\nAll connections obtained. Sleeping 5s to simulate usage...\n");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace(out);
        }

        for (int i = 0; i < connections.size(); i++) {
            try {
                connections.get(i).close();
                out.println("Returned connection " + (i + 1) + " to pool.");
            } catch (SQLException e) {
                e.printStackTrace(out);
            }
        }

        out.println("\nStress test finished.");
    }
}
