package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Showtime;

public class ShowtimeMapper {

    public static Showtime mapFullShowtime(ResultSet rs) throws SQLException {
        return new Showtime(
            rs.getInt("ShowtimeId"),
            rs.getInt("MovieId"),
            rs.getInt("RoomId"),
            rs.getString("StartTime"),
            rs.getString("EndTime"),
            rs.getDouble("TicketPrice")
        );
    }

    public static Showtime mapShowtimeWithMovieId(ResultSet rs, int movieId) throws SQLException {
        return new Showtime(
            rs.getInt("ShowtimeId"),
            movieId,
            rs.getInt("RoomId"),
            rs.getString("StartTime"),
            rs.getString("EndTime"),
            rs.getDouble("TicketPrice")
        );
    }
}
