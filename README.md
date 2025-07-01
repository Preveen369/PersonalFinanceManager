# 📘 Personal Finance Manager (Console-Based Java & MySQL Application)


## 📄 1. Project Overview

The *Personal Finance Manager* is a modular, menu-driven Java console application built to help individuals track, analyze, and manage their finances across multiple accounts and categories. Developed with JDBC and backed by MySQL, it provides a structured workflow for users to log transactions, monitor budgets, and export financial summaries—all in an offline, framework-free environment.

---

## 🎯 2. Objectives

- Design a clear, interactive interface for personal finance operations  
- Enable CRUD operations on accounts, categories, and transactions  
- Support monthly budget goals and detect overspending with alerts  
- Provide insight-rich financial reports (top categories, net savings, etc.)  
- Offer `.csv` export of transaction logs and breakdown summaries  
- Promote modular and scalable backend architecture in Java  

---

## 🧱 3. System Architecture

### 🔹 Layered Design

| Layer       | Description                                        |
|-------------|----------------------------------------------------|
| `auth`      | Registration, login logic, session handling        |
| `dao`       | JDBC-based data access for User, Account, Category, Transaction, Budget |
| `models`    | Plain Old Java Objects (POJOs) representing data entities |
| `service`   | Business logic: analytics, validation, budgeting   |
| `view`      | Console menus, reports, user interactions          |
| `util`      | Input handling, DB connection, hashing, date tools |

---

## 🧪 4. Functional Modules

| Module                | Responsibilities                                                   |
|-----------------------|--------------------------------------------------------------------|
| Authentication        | User registration, login, and session tracking                    |
| Account Management    | Add, list, or delete financial accounts per user                  |
| Category Management   | Add/view/delete categories (e.g., Groceries, Salary, Utilities)   |
| Transactions          | Record/edit/remove income & expenses with notes & timestamps      |
| Budget Goals & Alerts | Set monthly budget limits and receive overspending warnings       |
| Reports & Insights    | Show net savings, top categories, and monthly summaries           |
| CSV Export            | Export transaction history and budget summaries to `.csv`         |

---

## 📂 5. Database Schema

**Database Name:** `personal_finance_db`

```sql
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255)
);

CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    name VARCHAR(100),
    balance DECIMAL(10,2),
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    name VARCHAR(100),
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT,
    category_id INT,
    amount DECIMAL(10,2),
    type VARCHAR(10),
    timestamp DATETIME,
    note TEXT,
    created_at DATETIME,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE budgets (
    budget_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    year INT,
    month INT,
    amount DECIMAL(10,2),
    created_at DATETIME,
    UNIQUE(user_id, year, month),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

---

## 🖥️ 6. Sample Output (Console Snapshot)

```
=== Personal Finance Manager ===
1. Login
2. Register
3. Exit

=== Dashboard ===
1. View Transactions
2. Add Transaction
3. View Top Categories
4. View Net Savings
5. Set Monthly Budget
6. Export to CSV
7. Manage Accounts
8. Manage Categories
9. Logout
```

---

## 🚀 7. How to Run

1. Install **Java 17+** and **MySQL Server**
2. Create the database and tables using the provided `schema.sql`
3. Configure your database credentials in `DBUtil.java`
4. Compile and launch the app:

```bash
javac -d bin src/personalfinancemanager/FinanceApp.java
java -cp bin personalfinancemanager.FinanceApp
```

---

## 🧾 8. CSV Export Example
A sample file will be saved inside the exports/ directory as:
```
transactions_20250701_113212.csv
```
Example contents:
```
TransactionID,Type,Amount,CategoryID,Timestamp,Note
1,EXPENSE,1200.00,3,2025-07-01 10:21:00,Monthly Electricity Bill
2,INCOME,5000.00,1,2025-07-01 09:00:00,Salary Credit
```

---

## 🤝 Contributing

Pull requests are welcome! Feel free to fork the repository and submit improvements.

**Contributions are welcome! Follow these steps:**
1. Fork the project.
2. Create a feature branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add feature description"
   ```
4. Push to the branch:
   ```bash
   git push origin feature-name
   ```
5. Open a pull request.

---

## 📧 Contact

For queries or suggestions:
- 📧 Email: spreveen123@gmail.com
- 🌐 LinkedIn: www.linkedin.com/in/preveen-s-17250529b/

---

## 🌟 Show your support

If you find this project helpful or interesting, please consider giving it a ⭐ on GitHub to show your support!

---
