package dao;

import model.Room;
import utils.DBConnection;
import utils.RoomMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

	public boolean addRoom(Room room) {
	    String sql = "INSERT INTO Room (CinemaId, RoomName, RoomType, CreatedAt) VALUES (?, ?, ?, GETDATE())";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, room.getCinemaId());
	        stmt.setString(2, room.getRoomName());
	        stmt.setString(3, room.getRoomType());

	        return stmt.executeUpdate() > 0;

	    } catch (SQLException e) {
	        System.out.println("Error adding room: " + e.getMessage());
	        return false;
	    }
	}


    // Get room by ID
    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM Room WHERE RoomId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return RoomMapper.map(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching room: " + e.getMessage());
        }
        return null;
    }

    // Get all rooms
    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM Room ORDER BY RoomId ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(RoomMapper.map(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching room list: " + e.getMessage());
        }
        return list;
    }

    public List<Room> getRoomsByCinemaId(int cinemaId) {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE CinemaId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cinemaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(RoomMapper.map(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching rooms by cinema: " + e.getMessage());
        }
        return list;
    }

    public boolean updateRoom(Room room) {
        String sql = "UPDATE Room SET CinemaId=?, RoomName=?, RoomType=? WHERE RoomId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, room.getCinemaId());
            stmt.setString(2, room.getRoomName());
            stmt.setString(3, room.getRoomType());
            stmt.setInt(4, room.getRoomId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating room: " + e.getMessage());
            return false;
        }
    }


    // 🔹 Delete room
    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM Room WHERE RoomId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error deleting room: " + e.getMessage());
            return false;
        }
    }
    
    public List<Room> searchRooms(Integer cinemaId, String keyword, String roomType) {
        List<Room> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT r.RoomId, r.RoomName, r.CinemaId, r.RoomType, " +
            "COUNT(s.SeatId) AS SeatCount " +
            "FROM Room r " +
            "LEFT JOIN Seat s ON r.RoomId = s.RoomId " +
            "WHERE 1=1"
        );

        if (cinemaId != null) sql.append(" AND r.CinemaId = ?");
        if (keyword != null && !keyword.trim().isEmpty()) sql.append(" AND r.RoomName LIKE ?");
        if (roomType != null && !roomType.trim().isEmpty()) sql.append(" AND r.RoomType = ?");

        sql.append(" GROUP BY r.RoomId, r.RoomName, r.CinemaId, r.RoomType");
        sql.append(" ORDER BY r.RoomName ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (cinemaId != null) stmt.setInt(index++, cinemaId);
            if (keyword != null && !keyword.trim().isEmpty())
                stmt.setString(index++, "%" + keyword.trim() + "%");
            if (roomType != null && !roomType.trim().isEmpty())
                stmt.setString(index++, roomType.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Room r = new Room();
                    r.setRoomId(rs.getInt("RoomId"));
                    r.setRoomName(rs.getString("RoomName"));
                    r.setCinemaId(rs.getInt("CinemaId"));
                    r.setRoomType(rs.getString("RoomType"));
                    r.setSeatCount(rs.getInt("SeatCount"));
                    list.add(r);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching rooms: " + e.getMessage());
        }

        return list;
    }
}