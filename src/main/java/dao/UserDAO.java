package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;
import utils.DBConnection;
import utils.UserMapper;

public class UserDAO {
	// Add user
	public boolean addUser(User user) {
		String sql = "INSERT INTO AppUser (FullName, Email, PasswordHash, Phone, Role, Status, CreatedAt) " 
				+ "VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
		try(Connection conn = DBConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, user.getFullName());
	            stmt.setString(2, user.getEmail());
	            stmt.setString(3, user.getPassword());
	            stmt.setString(4, user.getPhone());
	            stmt.setString(5, user.getRole());
	            stmt.setString(6, user.getStatus());
	            
	            int rows = stmt.executeUpdate();
	            return rows > 0;
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Login
	public User login(String email, String password) {
		String sql = "SELECT * FROM AppUser WHERE Email = ? AND PasswordHash = ? AND Status = 'active'";
		try(Connection conn = DBConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, email);
	            stmt.setString(2, password);
	            
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) return UserMapper.mapUser(rs);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Get user by ID
	public User getUserByID(int userID) {
		String sql = "SELECT * FROM AppUser WHERE UserId = ?";
		try(Connection conn = DBConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, userID);
			
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return UserMapper.mapUser(rs);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM AppUser ORDER BY CreatedAt DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) users.add(UserMapper.mapUser(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
    
    // Update users
    public boolean updateUser(User user) {
        String sql = "UPDATE AppUser SET FullName=?, Email=?, Phone=?, Role=?, Status=? WHERE UserId=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getStatus());
            stmt.setInt(6, user.getUserId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete users
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM AppUser WHERE UserId=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}