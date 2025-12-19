package dao;

import model.Seat;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {
	
    public Seat getSeatById(int seatId) {
        String sql = "SELECT * FROM Seat WHERE SeatId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seatId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapSeat(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all seats
    public List<Seat> getAllSeats() {
        List<Seat> list = new ArrayList<>();
        String sql = "SELECT * FROM Seat ORDER BY SeatId ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapSeat(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Seat> getSeatsByRoomId(int roomId, String keyword, String status, String seatType) {
        List<Seat> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT * FROM Seat WHERE RoomId = ?"
        );

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND SeatNumber LIKE ?");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND Status = ?");
        }

        if (seatType != null && !seatType.trim().isEmpty()) {
            sql.append(" AND SeatType = ?");
        }

        sql.append(" ORDER BY SeatNumber ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            stmt.setInt(index++, roomId);

            if (keyword != null && !keyword.trim().isEmpty()) {
                stmt.setString(index++, "%" + keyword.trim() + "%");
            }

            if (status != null && !status.trim().isEmpty()) {
                stmt.setString(index++, status);
            }

            if (seatType != null && !seatType.trim().isEmpty()) {
                stmt.setString(index++, seatType);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSeat(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addSeat(Seat seat) {
        String sql = "INSERT INTO Seat (RoomId, SeatNumber, SeatType, Status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seat.getRoomId());
            stmt.setString(2, seat.getSeatNumber());
            stmt.setString(3, seat.getSeatType());
            stmt.setString(4, seat.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update seat
    public boolean updateSeat(Seat seat) {
        String sql = "UPDATE Seat SET RoomId = ?, SeatNumber = ?, SeatType = ?, Status = ? WHERE SeatId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seat.getRoomId());
            stmt.setString(2, seat.getSeatNumber());
            stmt.setString(3, seat.getSeatType());
            stmt.setString(4, seat.getStatus());
            stmt.setInt(5, seat.getSeatId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete seat
    public boolean deleteSeat(int seatId) {
        String sql = "DELETE FROM Seat WHERE SeatId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seatId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSeatStatus(int seatId, String newStatus) {
        String sql = "UPDATE Seat SET Status = ? WHERE SeatId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, seatId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Seat mapSeat(ResultSet rs) throws SQLException {
        Seat seat = new Seat();
        seat.setSeatId(rs.getInt("SeatId"));
        seat.setRoomId(rs.getInt("RoomId"));
        seat.setSeatNumber(rs.getString("SeatNumber"));
        seat.setSeatType(rs.getString("SeatType"));
        seat.setStatus(rs.getString("Status"));
        return seat;
    }
}