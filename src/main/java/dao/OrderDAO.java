package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.OrderDTO;
import utils.DBConnection;

public class OrderDAO {

	
	public boolean deleteHoldTicket(int ticketId, int userId) {
	    String sql = """
	        DELETE FROM Ticket
	        WHERE TicketId = ?
	          AND UserId = ?
	          AND Status = 'HOLD'
	    """;

	    try (Connection c = DBConnection.getConnection();
	         PreparedStatement ps = c.prepareStatement(sql)) {

	        ps.setInt(1, ticketId);
	        ps.setInt(2, userId);

	        return ps.executeUpdate() > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	
    public List<OrderDTO> getOrdersByUser(int userId) {
        List<OrderDTO> list = new ArrayList<>();
        String sql = """
        	    SELECT
        	        t.TicketId,
        	        m.Title,
        	        s.StartTime,
        	        t.Price,
        	        t.BookingTime,
        	        t.Status,
        	        se.SeatNumber
        	    FROM Ticket t
        	    JOIN Showtime s ON t.ShowtimeId = s.ShowtimeId
        	    JOIN Movie m ON s.MovieId = m.MovieId
        	    LEFT JOIN Seat se ON t.SeatId = se.SeatId
        	    WHERE t.UserId = ?
        	    ORDER BY t.BookingTime DESC
        	""";


        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDTO o = new OrderDTO();
                o.setTicketId(rs.getInt("TicketId"));
                o.setMovieTitle(rs.getString("Title"));
                o.setShowtime(rs.getTimestamp("StartTime"));
                o.setPrice(rs.getInt("Price"));
                o.setBookingTime(rs.getTimestamp("BookingTime"));
                o.setStatus(rs.getString("Status"));
                o.setSeatLabel(rs.getString("SeatNumber"));
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
 public int createOrder(int userId) {

	    String sql = """
	        INSERT INTO Orders (UserId, TotalAmount, Status)
	    	VALUES (?, 0, 'PENDING')
	    """;

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps =
	             conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	        ps.setInt(1, userId);
	        ps.executeUpdate();

	        ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()) return rs.getInt(1);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return -1;
	}

 public void updateTotalAmount(int orderId, int total) {
	    String sql = """
	        UPDATE Orders
	        SET TotalAmount = ?
	        WHERE OrderId = ?
	    """;

	    try (Connection c = DBConnection.getConnection();
	         PreparedStatement ps = c.prepareStatement(sql)) {

	        ps.setInt(1, total);
	        ps.setInt(2, orderId);
	        ps.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
 public void updatePaymentMethod(int orderId, String method) {
	    String sql = """
	        UPDATE Orders
	        SET PaymentMethod = ?
	        WHERE OrderId = ?
	    """;

	    try (Connection c = DBConnection.getConnection();
	         PreparedStatement ps = c.prepareStatement(sql)) {

	        ps.setString(1, method);
	        ps.setInt(2, orderId);
	        ps.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}