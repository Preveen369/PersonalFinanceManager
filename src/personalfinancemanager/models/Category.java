package personalfinancemanager.models;

import java.time.LocalDateTime;

public class Category {
    private int categoryId;
    private int userId;
    private String name;
    private LocalDateTime createdAt;

    public Category() {}

    public Category(int categoryId, int userId, String name, LocalDateTime createdAt) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.name = name;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "[Category] ID: " + categoryId +
               ", Name: " + name +
               ", Created: " + createdAt;
    }

}
