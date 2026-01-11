package dao;

import model.Cinema;
import utils.DBConnection;
import utils.CinemaMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CinemaDAO {

	public List<String> getCitiesByMovie(int movieId) {
	    List<String> list = new ArrayList<>();

	    String sql = """
	        SELECT DISTINCT c.City
	        FROM Cinema c
	        JOIN Room r ON r.CinemaId = c.CinemaId
	        JOIN Showtime s ON s.RoomId = r.RoomId
	        WHERE s.MovieId = ?
	    """;

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, movieId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            list.add(rs.getString("City"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}

	public List<Cinema> getCinemasByMovieAndCity(int movieId, String city) {
	    List<Cinema> list = new ArrayList<>();

	    String sql = """
	        SELECT DISTINCT c.CinemaId, c.Name
	        FROM Cinema c
	        JOIN Room r ON r.CinemaId = c.CinemaId
	        JOIN Showtime s ON s.RoomId = r.RoomId
	        WHERE s.MovieId = ?
	          AND c.City = ?
	    """;

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, movieId);
	        ps.setString(2, city);

	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            Cinema c = new Cinema();
	            c.setCinemaId(rs.getInt("CinemaId"));
	            c.setName(rs.getString("Name"));
	            list.add(c);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}

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
    
    // ✅ PHƯƠNG THỨC ĐÃ CẬP NHẬT: Lấy danh sách thành phố độc lập (DISTINCT Cities)
    public List<String> getDistinctCities() {
        List<String> cities = new ArrayList<>();
        // Sử dụng TRIM(City) trong SQL (giả sử DB hỗ trợ như SQL Server/MySQL) và kiểm tra chuỗi trống
        String sql = "SELECT DISTINCT TRIM(City) as City FROM Cinema WHERE City IS NOT NULL AND TRIM(City) <> '' ORDER BY City ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Lấy giá trị đã được TRIM (hoặc cột City nếu TRIM không hoạt động)
                String city = rs.getString("City"); 
                
                // Cần kiểm tra lại và trim() lần nữa trong Java để chắc chắn
                if (city != null) {
                    cities.add(city.trim());
                }
            }
            
            // Debug: Giúp bạn kiểm tra Console xem DAO có lấy được dữ liệu không
            System.out.println("DEBUG (CinemaDAO): Found " + cities.size() + " distinct cities."); 

        } catch (SQLException e) {
            System.out.println("❌ ERROR fetching distinct cities (Check DB connection and TRIM function): " + e.getMessage());
            // In stack trace để tìm lỗi kết nối/query
            e.printStackTrace();
        }
        return cities;
    }
    
    // ✅ PHƯƠNG THỨC GIỮ NGUYÊN: Lấy danh sách Rạp theo Thành phố
    public List<Cinema> getCinemasByCity(String city) {
        List<Cinema> list = new ArrayList<>();
        String sql = "SELECT * FROM Cinema WHERE City = ? ORDER BY Name ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Trim giá trị city trước khi truyền vào query
            stmt.setString(1, city != null ? city.trim() : "");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(CinemaMapper.map(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching cinemas by city: " + e.getMessage());
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
            // Trim() giá trị City khi cập nhật
            stmt.setString(3, cinema.getCity() != null ? cinema.getCity().trim() : null); 
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
    
    public List<Cinema> getCinemasByMovie(int movieId) {
        String sql = """
            SELECT DISTINCT c.*
            FROM Cinema c
            JOIN Room r ON c.CinemaId = r.CinemaId
            JOIN Showtime s ON r.RoomId = s.RoomId
            WHERE s.MovieId = ?
        """;

        List<Cinema> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(CinemaMapper.map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean hasDependentData(int cinemaId) {
        String sql = "SELECT COUNT(*) FROM Room WHERE CinemaId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cinemaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}