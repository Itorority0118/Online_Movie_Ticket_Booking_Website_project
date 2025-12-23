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
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapSeat(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Seat> getSeatsByRoomId(int roomId) {
        List<Seat> list = new ArrayList<>();
        String sql = """
            SELECT * FROM Seat
            WHERE RoomId = ?
            ORDER BY SeatRow, SeatCol
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapSeat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Seat> getSeatsByRoomAndShowtime(int roomId, int showtimeId) {
        List<Seat> list = new ArrayList<>();

        String sql = """
            SELECT 
                s.SeatId,
                s.RoomId,
                s.SeatNumber,
                s.SeatRow,
                s.SeatCol,
                s.SeatType,
                CASE 
                    WHEN t.TicketId IS NULL THEN 'Available'
                    ELSE 'Booked'
                END AS Status
            FROM Seat s
            LEFT JOIN Ticket t 
                ON s.SeatId = t.SeatId
                AND t.ShowtimeId = ?
                AND t.Status = 'Booked'
            WHERE s.RoomId = ?
            ORDER BY s.SeatRow, s.SeatCol
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, showtimeId);
            ps.setInt(2, roomId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapSeat(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addSeat(Seat seat) {
        String sql = """
            INSERT INTO Seat
            (RoomId, SeatNumber, SeatRow, SeatCol, SeatType, Status)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seat.getRoomId());
            ps.setString(2, seat.getSeatNumber());
            ps.setString(3, seat.getSeatRow());
            ps.setInt(4, seat.getSeatCol());
            ps.setString(5, seat.getSeatType());
            ps.setString(6, seat.getStatus());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSeat(Seat seat) {
        String sql = """
            UPDATE Seat
            SET RoomId = ?, SeatNumber = ?, SeatRow = ?, SeatCol = ?,
                SeatType = ?, Status = ?
            WHERE SeatId = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seat.getRoomId());
            ps.setString(2, seat.getSeatNumber());
            ps.setString(3, seat.getSeatRow());
            ps.setInt(4, seat.getSeatCol());
            ps.setString(5, seat.getSeatType());
            ps.setString(6, seat.getStatus());
            ps.setInt(7, seat.getSeatId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSeat(int seatId) {
        String sql = "DELETE FROM Seat WHERE SeatId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seatId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Seat mapSeat(ResultSet rs) throws SQLException {
        return new Seat(
            rs.getInt("SeatId"),
            rs.getInt("RoomId"),
            rs.getString("SeatNumber"),
            rs.getString("SeatRow"),
            rs.getInt("SeatCol"),
            rs.getString("SeatType"),
            rs.getString("Status")
        );
    }
}