package dao;

import model.Room;
import utils.DBConnection;
import utils.RoomMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // Add a new room
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO Room (CinemaId, RoomName, SeatCount, RoomType, CreatedAt) VALUES (?, ?, ?, ?, GETDATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, room.getCinemaId());
            stmt.setString(2, room.getRoomName());
            stmt.setInt(3, room.getSeatCount());
            stmt.setString(4, room.getRoomType());

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
        String sql = "SELECT * FROM Room ORDER BY RoomName ASC";
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

    // Get rooms by CinemaId (important for showing all rooms in a cinema)
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

    // Update room
    public boolean updateRoom(Room room) {
        String sql = "UPDATE Room SET CinemaId=?, RoomName=?, SeatCount=?, RoomType=? WHERE RoomId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, room.getCinemaId());
            stmt.setString(2, room.getRoomName());
            stmt.setInt(3, room.getSeatCount());
            stmt.setString(4, room.getRoomType());
            stmt.setInt(5, room.getRoomId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating room: " + e.getMessage());
            return false;
        }
    }

    // Delete room
    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM Room WHERE RoomId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting room: " + e.getMessage());
            return false;
        }
    }
}
