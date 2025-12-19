package dao;

import model.Cinema;
import utils.DBConnection;
import utils.CinemaMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CinemaDAO {

    // Add a new cinema
    public boolean addCinema(Cinema cinema) {
        String sql = "INSERT INTO Cinema (Name, Address, City, Phone, CreatedAt) VALUES (?, ?, ?, ?, GETDATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cinema.getName());
            stmt.setString(2, cinema.getAddress());
            stmt.setString(3, cinema.getCity());
            stmt.setString(4, cinema.getPhone());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding cinema: " + e.getMessage());
            return false;
        }
    }

    // Get cinema by ID
    public Cinema getCinemaById(int id) {
        String sql = "SELECT * FROM Cinema WHERE CinemaId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return CinemaMapper.map(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching cinema: " + e.getMessage());
        }
        return null;
    }

    // Get all cinemas
    public List<Cinema> getAllCinemas() {
        List<Cinema> list = new ArrayList<>();
        String sql = "SELECT * FROM Cinema ORDER BY CinemaId ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(CinemaMapper.map(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching cinema list: " + e.getMessage());
        }
        return list;
    }

    // Update cinema
    public boolean updateCinema(Cinema cinema) {
        String sql = "UPDATE Cinema SET Name=?, Address=?, City=?, Phone=? WHERE CinemaId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cinema.getName());
            stmt.setString(2, cinema.getAddress());
            stmt.setString(3, cinema.getCity());
            stmt.setString(4, cinema.getPhone());
            stmt.setInt(5, cinema.getCinemaId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating cinema: " + e.getMessage());
            return false;
        }
    }

    // Delete cinema
    public boolean deleteCinema(int id) {
        String sql = "DELETE FROM Cinema WHERE CinemaId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting cinema: " + e.getMessage());
            return false;
        }
    }

    public List<Cinema> searchCinemas(String keyword, String city) {
        List<Cinema> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Cinema WHERE 1=1");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND Name LIKE ?");
        }

        if (city != null && !city.trim().isEmpty()) {
            sql.append(" AND City = ?");
        }

        sql.append(" ORDER BY CinemaId ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                stmt.setString(index++, "%" + keyword.trim() + "%");
            }

            if (city != null && !city.trim().isEmpty()) {
                stmt.setString(index++, city.trim());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(CinemaMapper.map(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching cinemas: " + e.getMessage());
        }

        return list;
    }
}