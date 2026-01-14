package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Seat;

public class SeatMapper {

    public static Seat mapFullSeat(ResultSet rs) throws SQLException {
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

    public static Seat mapSeatForShowtime(ResultSet rs) throws SQLException {
        Seat seat = new Seat();
        seat.setSeatId(rs.getInt("SeatId"));
        seat.setSeatRow(rs.getString("SeatRow"));
        seat.setSeatCol(rs.getInt("SeatCol"));
        seat.setSeatType(rs.getString("SeatType"));
        seat.setStatus(rs.getString("Status"));
        return seat;
    }
}
