package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Ticket;

public class TicketMapper {

    public static Ticket mapFull(ResultSet rs) throws SQLException {
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

    public static Ticket mapIdAndPrice(ResultSet rs) throws SQLException {
        Ticket t = new Ticket();
        t.setTicketId(rs.getInt("TicketId"));
        t.setPrice(rs.getDouble("Price"));
        return t;
    }
}
