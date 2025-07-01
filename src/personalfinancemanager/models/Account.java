package personalfinancemanager.models;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class Account {
    private int accountId;
    private int userId;
    private String name;
    private double balance;
    private LocalDateTime createdAt;

    public Account() {}

    public Account(int accountId, int userId, String name, double balance, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.userId = userId;
        this.name = name;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "[Account] ID: " + accountId +
               ", Name: " + name +
               ", Balance: ₹" + String.format("%.2f", balance) +
               ", Created: " + createdAt;
    }

}
