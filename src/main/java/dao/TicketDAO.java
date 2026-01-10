package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Ticket;
import utils.DBConnection;

public class TicketDAO {
	
	public List<HashMap<String, Object>> getBookedTicketsByUser(int userId) {

	    List<HashMap<String, Object>> list = new ArrayList<>();

	    String sql = """
	        SELECT 
	            t.TicketId,
	            t.Price,
	            t.BookingTime,
	            m.Title AS MovieTitle,
	            s.SeatNumber AS SeatLabel,
	            st.StartTime AS StartTime,
	            r.RoomName AS Room,
	            c.Name AS Cinema
	        FROM Ticket t
	        JOIN Showtime st ON t.ShowtimeId = st.ShowtimeId
	        JOIN Movie m ON st.MovieId = m.MovieId
	        JOIN Room r ON st.RoomId = r.RoomId
	        JOIN Cinema c ON r.CinemaId = c.CinemaId
	        JOIN Seat s ON t.SeatId = s.SeatId
	        WHERE t.UserId = ?
	          AND t.Status = 'Booked'
	        ORDER BY t.BookingTime DESC
	    """;

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, userId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {

	            HashMap<String, Object> item = new HashMap<>();

	            item.put("id", rs.getInt("TicketId"));
	            item.put("movie", rs.getString("MovieTitle"));
	            item.put("seat", rs.getString("SeatLabel"));
	            item.put("price", rs.getDouble("Price"));
	            item.put("room", rs.getString("Room"));
	            item.put("cinema", rs.getString("Cinema"));
	            java.sql.Timestamp ts = rs.getTimestamp("StartTime");
	            if (ts != null) {
	                String iso = ts.toLocalDateTime().toString();
	                item.put("startTime", iso);
	                item.put("showDate", iso);
	            } else {
	                item.put("startTime", null);
	                item.put("showDate", null);
	            }

	            list.add(item);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	public List<Ticket> getTicketsByOrderId(
	        int orderId,
	        Connection conn
	) throws SQLException {

	    List<Ticket> list = new ArrayList<>();

	    String sql = "SELECT TicketId, Price FROM Ticket WHERE OrderId = ?";

	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, orderId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Ticket t = new Ticket();
	            t.setTicketId(rs.getInt("TicketId"));
	            t.setPrice(rs.getDouble("Price"));
	            list.add(t);
	        }
	    }
	    return list;
	}
	
	public int sumHoldTicketPrice(
	        int userId,
	        Connection conn
	) throws SQLException {

	    String sql = """
	        SELECT COALESCE(SUM(Price), 0)
	        FROM Ticket
	        WHERE UserId = ?
	          AND Status = 'HOLD'
	    """;

	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, userId);
	        ResultSet rs = ps.executeQuery();
	        return rs.next() ? rs.getInt(1) : 0;
	    }
	}

	
	public List<Ticket> holdTickets(
	        int userId,
	        int showtimeId,
	        List<Integer> seatIds) {

	    List<Ticket> tickets = new ArrayList<>();

	    String insertSql = """
	        INSERT INTO Ticket
	        (UserId, ShowtimeId, SeatId, Price, BookingTime, Status)
	        VALUES (?, ?, ?, ?, GETDATE(), 'HOLD')
	    """;

	    try (Connection conn = DBConnection.getConnection()) {
	        conn.setAutoCommit(false);

	        for (int seatId : seatIds) {

	            // ✅ TÍNH GIÁ CHUẨN THEO GHẾ
	            double price = calculateTicketPrice(conn, showtimeId, seatId);

	            Ticket t = new Ticket();
	            t.setUserId(userId);
	            t.setShowtimeId(showtimeId);
	            t.setSeatId(seatId);
	            t.setPrice(price);
	            t.setStatus("HOLD");

	            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
	                ps.setInt(1, userId);
	                ps.setInt(2, showtimeId);
	                ps.setInt(3, seatId);
	                ps.setDouble(4, price);
	                ps.executeUpdate();
	            }

	            tickets.add(t);
	        }

	        conn.commit();
	        return tickets;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}


	public boolean confirmHoldTickets(
	        int userId,
	        int orderId,
	        Connection conn
	) throws SQLException {

	    String sql = """
	        UPDATE Ticket
	        SET
	            Status = 'Booked',
	            OrderId = ?,
	            BookingTime = GETDATE()
	        WHERE
	            UserId = ?
	            AND Status = 'HOLD'
	    """;

	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, orderId);
	        ps.setInt(2, userId);

	        int updated = ps.executeUpdate();
	        System.out.println("DEBUG confirmHoldTickets updated=" + updated);
	        return updated > 0;
	    }
	}
	
	public void deleteExpiredHoldTickets() {

	    String sql = """
	        DELETE FROM Ticket
	        WHERE Status = 'HOLD'
	        AND DATEDIFF(MINUTE, BookingTime, GETDATE()) >= 5
	    """;

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        int rows = ps.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
//	public boolean holdSeats(int userId, int showtimeId, List<Integer> seatIds) {
//
//	    String sql = """
//	        INSERT INTO Ticket
//	        (UserId, ShowtimeId, SeatId, Price, BookingTime, Status)
//	        VALUES (?, ?, ?, 0, GETDATE(), 'HOLD')
//	    """;
//
//	    try (Connection conn = DBConnection.getConnection()) {
//	        conn.setAutoCommit(false);
//
//	        for (int seatId : seatIds) {
//	            try (PreparedStatement ps = conn.prepareStatement(sql)) {
//	                ps.setInt(1, userId);
//	                ps.setInt(2, showtimeId);
//	                ps.setInt(3, seatId);
//	                ps.executeUpdate();
//	            }
//	        }
//
//	        conn.commit();
//	        return true;
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return false;
//	    }
//	}


	private double calculateTicketPrice(
	        Connection conn,
	        int showtimeId,
	        int seatId
	) throws Exception {

	    String sql = """
	        SELECT 
	            st.TicketPrice +
	            CASE s.SeatType
	                WHEN 'VIP' THEN 30000
	                WHEN 'DOUBLE' THEN 50000
	                ELSE 0
	            END AS FinalPrice
	        FROM Showtime st
	        JOIN Seat s ON s.SeatId = ?
	        WHERE st.ShowtimeId = ?
	    """;

	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, seatId);
	        ps.setInt(2, showtimeId);

	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) return rs.getDouble("FinalPrice");
	    }
	    throw new Exception("Cannot calculate price");
	}

	public boolean bookTickets(
	        int userId,
	        int showtimeId,
	        List<Integer> seatIds
	) {
	    String checkSql = """
	        SELECT COUNT(*) 
	        FROM Ticket 
	        WHERE ShowtimeId = ? 
	        AND SeatId = ? 
	        AND Status = 'Booked'
	    """;

	    String insertSql = """
	        INSERT INTO Ticket
	        (UserId, ShowtimeId, SeatId, Price, BookingTime, Status)
	        VALUES (?, ?, ?, ?, GETDATE(), 'Booked')
	    """;

	    try (Connection conn = DBConnection.getConnection()) {
	        conn.setAutoCommit(false);

	        for (int seatId : seatIds) {

	            // 🔒 CHECK LOCK
	            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
	                check.setInt(1, showtimeId);
	                check.setInt(2, seatId);

	                ResultSet rs = check.executeQuery();
	                rs.next();
	                if (rs.getInt(1) > 0) {
	                    conn.rollback();
	                    return false; // ghế đã bị book
	                }
	            }

	            // 💰 TÍNH GIÁ (DÙNG CHUNG CONNECTION)
	            double price = calculateTicketPrice(conn, showtimeId, seatId);

	            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
	                ps.setInt(1, userId);
	                ps.setInt(2, showtimeId);
	                ps.setInt(3, seatId);
	                ps.setDouble(4, price);
	                ps.executeUpdate();
	            }
	        }

	        conn.commit();
	        return true;

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
    
    public double calculateTicketPrice(int showtimeId, int seatId) {

        String sql = """
            SELECT 
                st.TicketPrice +
                CASE s.SeatType
                    WHEN 'VIP' THEN 30000
                    WHEN 'DOUBLE' THEN 50000
                    ELSE 0
                END AS FinalPrice
            FROM Showtime st
            JOIN Seat s ON s.SeatId = ?
            WHERE st.ShowtimeId = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seatId);
            ps.setInt(2, showtimeId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("FinalPrice");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // fallback (KHÔNG BAO GIỜ NÊN XẢY RA)
        return 0;
    }

    
    public boolean createTicket(Ticket ticket) {

        String sql = """
            INSERT INTO Ticket
            (UserId, ShowtimeId, SeatId, Price, BookingTime, Status)
            VALUES (?, ?, ?, ?, GETDATE(), 'Booked')
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ticket.getUserId());
            ps.setInt(2, ticket.getShowtimeId());
            ps.setInt(3, ticket.getSeatId());
            ps.setDouble(4, ticket.getPrice());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int countTicketsByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Ticket WHERE Status = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public List<HashMap<String, Object>> getHoldingTicketDetails(int userId) {

        List<HashMap<String, Object>> list = new ArrayList<>();

        String sql = """
            SELECT 
                t.TicketId,
                t.Price,
                t.BookingTime,
                m.Title AS MovieTitle,
                s.SeatNumber AS SeatLabel,
                st.StartTime AS StartTime,
                r.RoomName AS Room,
                c.Name AS Cinema
            FROM Ticket t
            JOIN Showtime st ON t.ShowtimeId = st.ShowtimeId
            JOIN Movie m ON st.MovieId = m.MovieId
            JOIN Room r ON st.RoomId = r.RoomId
            JOIN Cinema c ON r.CinemaId = c.CinemaId
            JOIN Seat s ON t.SeatId = s.SeatId
            WHERE t.UserId = ?
              AND t.Status = 'HOLD'
            ORDER BY t.BookingTime DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                HashMap<String, Object> item = new HashMap<>();

                item.put("id", rs.getInt("TicketId"));
                item.put("movie", rs.getString("MovieTitle"));
                item.put("seat", rs.getString("SeatLabel"));
                item.put("price", rs.getDouble("Price"));
                item.put("room", rs.getString("Room"));
                item.put("cinema", rs.getString("Cinema"));
                item.put("startTime", rs.getTimestamp("StartTime"));
                item.put("showDate", rs.getTimestamp("StartTime"));
                item.put("holdTime", rs.getTimestamp("BookingTime").getTime());

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}