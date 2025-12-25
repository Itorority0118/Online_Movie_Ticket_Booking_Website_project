package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Ticket;
import utils.DBConnection;

public class TicketDAO {

	public boolean bookTicket(Ticket ticket) {

	    double finalPrice = calculateTicketPrice(
	        ticket.getShowtimeId(),
	        ticket.getSeatId()
	    );

	    String sql = """
	        INSERT INTO Ticket
	        (UserID, ShowtimeId, SeatId, Price, BookingTime, Status)
	        VALUES (?, ?, ?, ?, ?, 'Booked')
	    """;

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, ticket.getUserId());
	        ps.setInt(2, ticket.getShowtimeId());
	        ps.setInt(3, ticket.getSeatId());
	        ps.setDouble(4, finalPrice);
	        ps.setString(5, ticket.getBookingTime());

	        return ps.executeUpdate() > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

    public boolean cancelTicket(int ticketId) {
        String sql = "UPDATE Ticket SET Status = 'Cancelled' WHERE TicketId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ticketId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTicket(Ticket ticket) {
        String sql = """
            UPDATE Ticket
            SET UserID = ?, ShowtimeId = ?, SeatId = ?, Price = ?, BookingTime = ?, Status = ?
            WHERE TicketId = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ticket.getUserId());
            ps.setInt(2, ticket.getShowtimeId());
            ps.setInt(3, ticket.getSeatId());
            ps.setDouble(4, ticket.getPrice());
            ps.setString(5, ticket.getBookingTime());
            ps.setString(6, ticket.getStatus());
            ps.setInt(7, ticket.getTicketId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Ticket getTicketById(int ticketId) {
        String sql = "SELECT * FROM Ticket WHERE TicketId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ticketId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Ticket(
                    rs.getInt("TicketId"),
                    rs.getInt("UserID"),
                    rs.getInt("ShowtimeId"),
                    rs.getInt("SeatId"),
                    rs.getDouble("Price"),
                    rs.getString("BookingTime"),
                    rs.getString("Status")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Ticket> getAllTickets() {
        List<Ticket> list = new ArrayList<>();
        String sql = "SELECT * FROM Ticket ORDER BY BookingTime ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ticket ticket = new Ticket(
                    rs.getInt("TicketId"),
                    rs.getInt("UserID"),
                    rs.getInt("ShowtimeId"),
                    rs.getInt("SeatId"),
                    rs.getDouble("Price"),
                    rs.getString("BookingTime"),
                    rs.getString("Status")
                );
                list.add(ticket);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean deleteTicket(int ticketId) {
        String sql = "DELETE FROM Ticket WHERE TicketId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ticketId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Ticket> filterTickets(
            Integer userId,
            Integer showtimeId,
            String status
    ) {
        List<Ticket> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT * FROM Ticket WHERE 1=1"
        );

        if (userId != null) {
            sql.append(" AND UserID = ?");
        }
        if (showtimeId != null) {
            sql.append(" AND ShowtimeId = ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status = ?");
        }

        sql.append(" ORDER BY TicketId ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (userId != null) ps.setInt(index++, userId);
            if (showtimeId != null) ps.setInt(index++, showtimeId);
            if (status != null && !status.isEmpty()) ps.setString(index++, status);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Ticket(
                    rs.getInt("TicketId"),
                    rs.getInt("UserID"),
                    rs.getInt("ShowtimeId"),
                    rs.getInt("SeatId"),
                    rs.getDouble("Price"),
                    rs.getString("BookingTime"),
                    rs.getString("Status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private double getShowtimeBasePrice(int showtimeId) {
        String sql = "SELECT Price FROM Showtime WHERE ShowtimeId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, showtimeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("Price");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private double getSeatExtraPrice(int seatId) {
        String sql = "SELECT SeatType FROM Seat WHERE SeatId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seatId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String type = rs.getString("SeatType");

                return switch (type) {
                    case "VIP" -> 20000;
                    case "DOUBLE" -> 50000;
                    default -> 0;
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private double calculateTicketPrice(int showtimeId, int seatId) {
        return getShowtimeBasePrice(showtimeId)
             + getSeatExtraPrice(seatId);
    }
    
    public int countSoldTickets() {
        String sql = """
            SELECT COUNT(*) 
            FROM Ticket
            WHERE Status IN ('Booked', 'Used')
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
}