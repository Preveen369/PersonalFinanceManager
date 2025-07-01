package personalfinancemanager.models;

import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private int accountId;
    private int categoryId;
    private double amount;
    private String type; // INCOME or EXPENSE
    private LocalDateTime timestamp;
    private String note;
    private LocalDateTime createdAt;

    public Transaction() {}

    public Transaction(int transactionId, int accountId, int categoryId, double amount, String type,
                       LocalDateTime timestamp, String note, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
        this.note = note;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "[Transaction] ID: " + transactionId +
               ", Type: " + type +
               ", Amount: ₹" + String.format("%.2f", amount) +
               ", Account: " + accountId +
               ", Category: " + categoryId +
               ", Date: " + timestamp.toLocalDate() +
               ", Note: " + (note != null ? note : "—");
    }

}
