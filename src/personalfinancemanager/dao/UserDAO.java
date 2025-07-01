package personalfinancemanager.dao;

import personalfinancemanager.models.User;
import personalfinancemanager.util.DBUtil;

import java.sql.*;

public class UserDAO implements IUserDAO {

    @Override
    public User findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password_hash")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean save(User user) {
        String query = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPasswordHash());
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
        return false;
    }
}

