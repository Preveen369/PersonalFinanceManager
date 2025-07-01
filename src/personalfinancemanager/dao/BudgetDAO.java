package personalfinancemanager.dao;

import personalfinancemanager.util.DBUtil;
import java.sql.*;

public class BudgetDAO implements IBudgetDAO {
    public boolean saveOrUpdate(int userId, int year, int month, double amount) {
        String sql = """
            INSERT INTO budgets (user_id, year, month, amount)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE amount = VALUES(amount)
        """;
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, userId);
            pst.setInt(2, year);
            pst.setInt(3, month);
            pst.setDouble(4, amount);
            return pst.executeUpdate() == 1 || pst.executeUpdate() == 2;
        } catch (SQLException e) {
            System.err.println("Budget save error: " + e.getMessage());
            return false;
        }
    }

    public Double getBudget(int userId, int year, int month) {
        String sql = "SELECT amount FROM budgets WHERE user_id = ? AND year = ? AND month = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, userId);
            pst.setInt(2, year);
            pst.setInt(3, month);
            ResultSet rs = pst.executeQuery();
            return rs.next() ? rs.getDouble("amount") : null;
        } catch (SQLException e) {
            System.err.println("Budget fetch error: " + e.getMessage());
            return null;
        }
    }
}
