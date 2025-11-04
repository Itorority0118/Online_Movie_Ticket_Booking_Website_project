package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Room;

public class RoomMapper {
    public static Room map(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("RoomId"));
        room.setCinemaId(rs.getInt("CinemaId"));
        room.setRoomName(rs.getString("RoomName"));
        room.setSeatCount(rs.getInt("SeatCount"));
        room.setRoomType(rs.getString("RoomType"));
        return room;
    }
}
