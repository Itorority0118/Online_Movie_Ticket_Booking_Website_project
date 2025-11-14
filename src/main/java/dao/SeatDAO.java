package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Seat;
import utils.DBConnection;

public class SeatDAO {

    // Lấy danh sách ghế theo suất chiếu
    public List<Seat> getSeatsByShowtime(int showtimeId) {
        List<Seat> seats = new ArrayList<>();
        String query = "SELECT seat_id, row, number, type, status FROM Seats WHERE showtime_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, showtimeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatId(rs.getInt("seat_id"));
                seat.setRoomId(rs.getInt("row"));
                seat.setSeatNumber(rs.getString("number"));
                seat.setSeatType(rs.getString("Type"));
                seat.setStatus(rs.getString("status"));
                seats.add(seat);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return seats;
    }

    // Cập nhật trạng thái ghế
    public boolean updateSeatStatus(int seatId, String newStatus) {
        String query = "UPDATE Seats SET status = ? WHERE seat_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, newStatus); // "booked", "available", v.v.
            ps.setInt(2, seatId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
