package personalfinancemanager.dao;

import personalfinancemanager.models.Account;
import personalfinancemanager.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements IAccountDAO {

    @Override
    public boolean save(Account account) {
        String sql = "INSERT INTO accounts (user_id, name, balance, created_at) VALUES (?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, account.getUserId());
            pst.setString(2, account.getName());
            pst.setDouble(3, account.getBalance());
            pst.setTimestamp(4, Timestamp.valueOf(account.getCreatedAt()));
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error saving account: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateBalance(int accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDouble(1, newBalance);
            pst.setInt(2, accountId);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error updating balance: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Account> findAllByUserId(int userId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                accounts.add(new Account(
                    rs.getInt("account_id"),
                    userId,
                    rs.getString("name"),
                    rs.getDouble("balance"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching accounts: " + e.getMessage());
        }
        return accounts;
    }

    @Override
    public Account findById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, accountId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getDouble("balance"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching account by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean delete(int accountId) {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, accountId);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error deleting account: " + e.getMessage());
            return false;
        }
    }
}

