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
	
	public List<OrderDTO> getOrderDTOByOrderId(int orderId, Connection conn) {
	    List<OrderDTO> list = new ArrayList<>();
	    String sql = """
	        SELECT
	            t.TicketId,
	            m.Title AS MovieTitle,
	            s.StartTime AS ShowTime,
	            r.RoomName,
	            c.Name,
	            t.Price,
	            t.BookingTime,
	            t.Status,
	            se.SeatNumber AS SeatLabel
	        FROM Ticket t
	        JOIN Showtime s ON t.ShowtimeId = s.ShowtimeId
	        JOIN Movie m ON s.MovieId = m.MovieId
	        JOIN Room r ON s.RoomId = r.RoomId
	        JOIN Cinema c ON r.CinemaId = c.CinemaId
	        LEFT JOIN Seat se ON t.SeatId = se.SeatId
	        WHERE t.OrderId = ?
	        ORDER BY t.BookingTime ASC
	    """;

	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, orderId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            OrderDTO o = new OrderDTO();
	            o.setTicketId(rs.getInt("TicketId"));
	            o.setMovieTitle(rs.getString("MovieTitle"));
	            o.setShowtime(rs.getTimestamp("ShowTime"));
	            o.setRoomName(rs.getString("RoomName"));
	            o.setCinemaName(rs.getString("Name"));
	            o.setPrice(rs.getInt("Price"));
	            o.setBookingTime(rs.getTimestamp("BookingTime"));
	            o.setStatus(rs.getString("Status"));
	            String seat = rs.getString("SeatLabel");
	            o.setSeatLabel(seat != null ? seat : "Chưa chọn ghế");
	            list.add(o);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}

    // Xóa ticket đang HOLD
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

    // Lấy danh sách orders của user
    public List<OrderDTO> getOrdersByUser(int userId) {
        List<OrderDTO> list = new ArrayList<>();
        String sql = """
            SELECT
                t.TicketId,
                m.Title AS MovieTitle,
                s.StartTime AS ShowTime,
                r.RoomName,
                c.CinemaName,
                t.Price,
                t.BookingTime,
                t.Status,
                se.SeatNumber AS SeatLabel
            FROM Ticket t
            JOIN Showtime s ON t.ShowtimeId = s.ShowtimeId
            JOIN Movie m ON s.MovieId = m.MovieId
            JOIN Room r ON s.RoomId = r.RoomId
            JOIN Cinema c ON r.CinemaId = c.CinemaId
            LEFT JOIN Seat se ON t.SeatId = se.SeatId
            WHERE t.UserId = ?
              AND t.Status IN ('Booked','Used','HOLD')
            ORDER BY t.BookingTime DESC
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDTO o = new OrderDTO();
                o.setTicketId(rs.getInt("TicketId"));
                o.setMovieTitle(rs.getString("MovieTitle"));
                o.setShowtime(rs.getTimestamp("ShowTime"));
                o.setRoomName(rs.getString("RoomName"));
                o.setCinemaName(rs.getString("CinemaName"));
                o.setPrice(rs.getInt("Price"));
                o.setBookingTime(rs.getTimestamp("BookingTime"));
                o.setStatus(rs.getString("Status"));
                String seat = rs.getString("SeatLabel");
                o.setSeatLabel(seat != null ? seat : "Chưa chọn ghế");
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tạo order mới, trả về orderId
    public int createOrder(int userId, Connection conn) throws Exception {
        String sql = """
            INSERT INTO Orders (UserId, TotalAmount, Status)
            VALUES (?, 0, 'PENDING')
        """;

        try (PreparedStatement ps =
                 conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }

        return -1;
    }

    // Cập nhật tổng tiền của order
    public void updateTotalAmount(int orderId, int total, Connection conn) throws Exception {
        String sql = """
            UPDATE Orders
            SET TotalAmount = ?
            WHERE OrderId = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, total);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    // Cập nhật phương thức thanh toán
    public void updatePaymentMethod(int orderId, String method, Connection conn) throws Exception {
        String sql = """
            UPDATE Orders
            SET PaymentMethod = ?
            WHERE OrderId = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, method);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }
}