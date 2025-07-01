package personalfinancemanager.dao;

import personalfinancemanager.models.Transaction;
import personalfinancemanager.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO implements ITransactionDAO {

    @Override
    public boolean save(Transaction tx) {
        String sql = "INSERT INTO transactions (account_id, category_id, amount, type, timestamp, note, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, tx.getAccountId());
            pst.setInt(2, tx.getCategoryId());
            pst.setDouble(3, tx.getAmount());
            pst.setString(4, tx.getType());
            pst.setTimestamp(5, Timestamp.valueOf(tx.getTimestamp()));
            pst.setString(6, tx.getNote());
            pst.setTimestamp(7, Timestamp.valueOf(tx.getCreatedAt()));

            return pst.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("Error inserting transaction: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Transaction> findByUser(int userId) {
        String sql = "SELECT t.* FROM transactions t " +
                     "JOIN accounts a ON t.account_id = a.account_id WHERE a.user_id = ? ORDER BY t.timestamp DESC";
        return fetchTransactions(sql, stmt -> stmt.setInt(1, userId));
    }

    @Override
    public List<Transaction> findByMonth(int userId, int year, int month) {
        String sql = "SELECT t.* FROM transactions t " +
                     "JOIN accounts a ON t.account_id = a.account_id " +
                     "WHERE a.user_id = ? AND YEAR(t.timestamp) = ? AND MONTH(t.timestamp) = ? " +
                     "ORDER BY t.timestamp DESC";
        return fetchTransactions(sql, stmt -> {
            stmt.setInt(1, userId);
            stmt.setInt(2, year);
            stmt.setInt(3, month);
        });
    }

    @Override
    public List<Transaction> findByCategory(int categoryId) {
        String sql = "SELECT * FROM transactions WHERE category_id = ? ORDER BY timestamp DESC";
        return fetchTransactions(sql, stmt -> stmt.setInt(1, categoryId));
    }

    @Override
    public double getTotalByType(int userId, String type) {
        String sql = "SELECT SUM(amount) FROM transactions t " +
                     "JOIN accounts a ON t.account_id = a.account_id " +
                     "WHERE a.user_id = ? AND t.type = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, userId);
            pst.setString(2, type);
            ResultSet rs = pst.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0.0;
        } catch (SQLException e) {
            System.err.println("Error getting total " + type + ": " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    public boolean delete(int transactionId) {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, transactionId);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateTransaction(int transactionId, double amount, String note) {
        String sql = "UPDATE transactions SET amount = ?, note = ? WHERE transaction_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDouble(1, amount);
            pst.setString(2, note);
            pst.setInt(3, transactionId);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error updating transaction: " + e.getMessage());
            return false;
        }
    }


    // 🔁 Helper method to reduce repetition in fetching logic
    private List<Transaction> fetchTransactions(String sql, SQLConsumer<PreparedStatement> paramSetter) {
        List<Transaction> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            paramSetter.accept(pst);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Transaction(
                    rs.getInt("transaction_id"),
                    rs.getInt("account_id"),
                    rs.getInt("category_id"),
                    rs.getDouble("amount"),
                    rs.getString("type"),
                    rs.getTimestamp("timestamp").toLocalDateTime(),
                    rs.getString("note"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
        }
        return list;
    }

    // ✅ Functional interface for lambda-based PreparedStatement parameter setting
    @FunctionalInterface
    private interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }
}
