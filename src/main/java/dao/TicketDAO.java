package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Ticket;
import utils.DBConnection;

public class TicketDAO {

    // Đặt vé mới
    public boolean bookTicket(Ticket ticket) {
        String query = "INSERT INTO Tickets (user_id, showtime_id, seat_id, price, booking_time, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, ticket.getUserId());
            ps.setInt(2, ticket.getShowtimeId());
            ps.setInt(3, ticket.getSeatId());
            ps.setDouble(4, ticket.getPrice());
            ps.setString(5, ticket.getBookingTime()); // ví dụ: "2025-10-20 18:45"
            ps.setString(6, ticket.getStatus());      // ví dụ: "Booked"

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hủy vé (cập nhật trạng thái)
    public boolean cancelTicket(int ticketId) {
        String query = "UPDATE Tickets SET status = 'Cancelled' WHERE ticket_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, ticketId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xem lịch sử vé của người dùng
    public List<Ticket> getTicketsByUser(int userId) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM Tickets WHERE user_id = ? ORDER BY booking_time DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ticket ticket = new Ticket(
                    rs.getInt("ticket_id"),
                    rs.getInt("user_id"),
                    rs.getInt("showtime_id"),
                    rs.getInt("seat_id"),
                    rs.getDouble("price"),
                    rs.getString("booking_time"),
                    rs.getString("status")
                );
                tickets.add(ticket);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickets;
    }
}
