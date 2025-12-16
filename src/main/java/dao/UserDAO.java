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

    public boolean addUser(User user) {
        String sql = "INSERT INTO AppUser (FullName, Email, Password, Phone, Role, Status, CreatedAt) "
                   + "VALUES (?, ?, ?, ?, ?, ?, GETDATE())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User login(String email, String password) {
        String sql = "SELECT * FROM AppUser WHERE LTRIM(RTRIM(Email)) = LTRIM(RTRIM(?)) " +
                     "AND LTRIM(RTRIM(Password)) = LTRIM(RTRIM(?)) " +
                     "AND Status = 'active'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return UserMapper.mapUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByID(int userID) {
        String sql = "SELECT * FROM AppUser WHERE UserId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return UserMapper.mapUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM AppUser ORDER BY CreatedAt DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(UserMapper.mapUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updateUser(User user) {
        String sql;

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            sql = "UPDATE AppUser SET FullName=?, Email=?, Phone=?, Role=?, Status=?, Password=? WHERE UserId=?";
        } else {
            sql = "UPDATE AppUser SET FullName=?, Email=?, Phone=?, Role=?, Status=? WHERE UserId=?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getStatus());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                stmt.setString(6, user.getPassword());
                stmt.setInt(7, user.getUserId());
            } else {
                stmt.setInt(6, user.getUserId());
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


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
    
    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE AppUser SET Password=? WHERE Email=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, email);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM AppUser WHERE Email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return UserMapper.mapUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM AppUser WHERE Email=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM AppUser";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<User> getUsersByRole(String role) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM AppUser WHERE Role = ? ORDER BY CreatedAt DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(UserMapper.mapUser(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> getUsersByStatus(String status) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM AppUser WHERE Status = ? ORDER BY CreatedAt DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(UserMapper.mapUser(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> searchUsers(String keyword) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM AppUser WHERE FullName LIKE ? OR Email LIKE ? ORDER BY CreatedAt DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            keyword = "%" + keyword + "%";
            stmt.setString(1, keyword);
            stmt.setString(2, keyword);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(UserMapper.mapUser(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countUsersByRole(String role) {
        String sql = "SELECT COUNT(*) FROM AppUser WHERE Role = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countUsersByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM AppUser WHERE Status = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public List<User> getUsersByRoleAndStatus(String role, String status) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM AppUser WHERE Role = ? AND Status = ? ORDER BY CreatedAt DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            stmt.setString(2, status);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(UserMapper.mapUser(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}