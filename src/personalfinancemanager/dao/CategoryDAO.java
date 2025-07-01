package personalfinancemanager.dao;

import personalfinancemanager.models.Category;
import personalfinancemanager.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements ICategoryDAO {

    @Override
    public boolean save(Category category) {
        String sql = "INSERT INTO categories (user_id, name, created_at) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, category.getUserId());
            pst.setString(2, category.getName());
            pst.setTimestamp(3, Timestamp.valueOf(category.getCreatedAt()));
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error saving category: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Category> findAllByUserId(int userId) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE user_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Category(
                    rs.getInt("category_id"),
                    userId,
                    rs.getString("name"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving categories: " + e.getMessage());
        }
        return list;
    }

    @Override
    public int findUserByCategoryId(int categoryId) {
        String sql = "SELECT user_id FROM categories WHERE category_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, categoryId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user ID by category ID: " + e.getMessage());
        }
        return -1; // Indicates not found or error
    }

    @Override
    public Category findByName(int userId, String name) {
        String sql = "SELECT * FROM categories WHERE user_id = ? AND name = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, userId);
            pst.setString(2, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Category(
                    rs.getInt("category_id"),
                    userId,
                    rs.getString("name"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding category: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean delete(int categoryId) {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, categoryId);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            return false;
        }
    }
}
