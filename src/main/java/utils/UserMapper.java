package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserMapper {
    public static User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("UserId"));
        user.setFullName(rs.getString("FullName"));
        user.setEmail(rs.getString("Email"));
        user.setPassword(rs.getString("PasswordHash"));
        user.setPhone(rs.getString("Phone"));
        user.setRole(rs.getString("Role"));
        user.setStatus(rs.getString("Status"));

        java.sql.Timestamp created = rs.getTimestamp("CreatedAt");
        if (created != null)
            user.setCreatedAt(created.toString());
        else
            user.setCreatedAt(null);

        return user;
    }
}
