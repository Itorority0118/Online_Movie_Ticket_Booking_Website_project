package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import model.OrderDTO;

public class OrderMapper {

    public static OrderDTO mapOrderDTO(ResultSet rs) throws SQLException {
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

        return o;
    }
}
