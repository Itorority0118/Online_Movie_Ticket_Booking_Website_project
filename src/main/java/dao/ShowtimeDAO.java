package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Showtime;
import utils.DBConnection;

public class ShowtimeDAO {
	
    public boolean addShowtime(Showtime showtime) {
        String query = "INSERT INTO Showtime (movie_id, room_id, start_time, end_time, ticket_price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, showtime.getMovieId());
            ps.setInt(2, showtime.getRoomId());
            ps.setString(3, showtime.getStartTime());
            ps.setString(4, showtime.getEndTime());
            ps.setDouble(5, showtime.getTicketPrice());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateShowtime(Showtime showtime) {
        String query = "UPDATE Showtime SET movie_id = ?, room_id = ?, start_time = ?, end_time = ?, ticket_price = ? WHERE showtime_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, showtime.getMovieId());
            ps.setInt(2, showtime.getRoomId());
            ps.setString(3, showtime.getStartTime());
            ps.setString(4, showtime.getEndTime());
            ps.setDouble(5, showtime.getTicketPrice());
            ps.setInt(6, showtime.getShowtimeId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteShowtime(int showtimeId) {
        String query = "DELETE FROM Showtime WHERE showtime_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, showtimeId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        String query = "SELECT * FROM Showtime";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Showtime showtime = new Showtime(
                    rs.getInt("showtime_id"),
                    rs.getInt("movie_id"),
                    rs.getInt("room_id"),
                    rs.getString("start_time"),
                    rs.getString("end_time"),
                    rs.getDouble("ticket_price")
                );
                showtimes.add(showtime);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return showtimes;
    }
    
    public boolean existsByMovieId(int movieId) {
        String query = "SELECT 1 FROM Showtime WHERE MovieId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}