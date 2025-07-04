package personalfinancemanager.view;

import personalfinancemanager.models.Transaction;
import personalfinancemanager.service.FinanceService;
import personalfinancemanager.service.ExportService;
import personalfinancemanager.util.ConsoleInput;
import personalfinancemanager.util.TablePrinter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportView {
    private final FinanceService financeService;

    public ReportView(FinanceService financeService) {
        this.financeService = financeService;
        this.exportService = new ExportService(financeService);
    }

    public void printAllTransactions(int userId) {
        List<Transaction> txList = financeService.getAllTransactions(userId);
        if (txList.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] { "ID", "Type", "Amount", "CategoryID", "Timestamp", "Note" });
        for (Transaction tx : txList) {
            rows.add(new String[] {
                String.valueOf(tx.getTransactionId()),
                tx.getType(),
                String.format("Rs.%.2f", tx.getAmount()),
                String.valueOf(tx.getCategoryId()),
                tx.getTimestamp().toString(),
                tx.getNote() != null ? tx.getNote() : "-"
            });
        }
        TablePrinter.printGrid(rows, new int[] { 4, 10, 10, 10, 20, 20 });
    }


    public void printMonthlyBreakdown(int userId) {
        int year = ConsoleInput.readInt("Enter year (e.g. 2025): ");
        int month = ConsoleInput.readInt("Enter month (1-12): ");

        Map<String, Double> breakdown = financeService.getMonthlyCategoryBreakdown(userId, year, month);
        if (breakdown.isEmpty()) {
            System.out.println("No expenses found for the selected month.");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] { "Category", "Amount" });
        for (var entry : breakdown.entrySet()) {
            rows.add(new String[] {
                entry.getKey(),
                String.format("%.2f", entry.getValue())
            });
        }
        TablePrinter.printGrid(rows, new int[] { 20, 12 });
    }


    public void printTopSpendingCategories(int userId) {
        int year = ConsoleInput.readInt("Enter year: ");
        int month = ConsoleInput.readInt("Enter month: ");

        var top = financeService.getTopCategories(userId, year, month);
        if (top.isEmpty()) {
            System.out.println("No data to rank categories.");
            return;
        }

        System.out.println("\n[OK] Top 3 Spending Categories:");
        int rank = 1;
        for (var entry : top) {
            System.out.printf("%d. %-20s Rs.%.2f\n", rank++, entry.getKey(), entry.getValue());
        }
    }

    public void printSavings(int userId) {
        double savings = financeService.getNetSavings(userId);
        String symbol = savings >= 0 ? "[OK]" : "[X]️";
        System.out.printf("\n%s Net Savings:  Rs.%.2f\n", symbol, savings);
    }
    
    public void printBudgetSummary(int userId) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        Double budget = financeService.getMonthlyBudget(userId, year, month);
        double spent = financeService.getMonthlyCategoryBreakdown(userId, year, month)
                        .values().stream().mapToDouble(Double::doubleValue).sum();

        if (budget == null) {
            System.out.println("[!] You haven’t set a budget for this month yet.");
        } else {
            String emoji = spent > budget ? "[OK]" : "[X]️";
            System.out.printf("%s Spent Rs.%.2f of Rs.%.2f for %s %d\n", emoji, spent, budget, now.getMonth(), year);
        }
    }

    
    public void promptNewTransaction(int userId) {
        System.out.println("\n=== Add New Transaction ===");

        // Show available accounts
        var accounts = financeService.getAccountsByUser(userId);
        if (accounts.isEmpty()) {
            System.out.println("[!] No accounts found. Create one first!");
            return;
        }
        System.out.println("[OK] Your Accounts:");
        System.out.printf("%-5s %-20s %-10s\n", "ID", "Name", "Balance");
        for (var acc : accounts) {
            System.out.printf("%-5d %-20s Rs.%.2f\n", acc.getAccountId(), acc.getName(), acc.getBalance());
        }

        // Show available categories
        var categories = financeService.getCategoriesByUser(userId);
        if (categories.isEmpty()) {
            System.out.println("[!]️ No categories available. Create one first!");
            return;
        }
        System.out.println("\n[OK] Your Categories:");
        System.out.printf("%-5s %-20s\n", "ID", "Name");
        for (var cat : categories) {
            System.out.printf("%-5d %-20s\n", cat.getCategoryId(), cat.getName());
        }

        // Proceed with transaction input
        int accountId = ConsoleInput.readInt("Account ID: ");
        int categoryId = ConsoleInput.readInt("Category ID: ");
        double amount = ConsoleInput.readDouble("Amount (Rs.): ");
        String type;
        do {
            type = ConsoleInput.readString("Type (INCOME or EXPENSE): ").toUpperCase();
        } while (!type.equals("INCOME") && !type.equals("EXPENSE"));

        LocalDate date = ConsoleInput.readDate("Transaction date");
        String note = ConsoleInput.readString("Note (optional): ");

        Transaction tx = new Transaction(
            0,
            accountId,
            categoryId,
            amount,
            type,
            date.atStartOfDay(),
            note,
            LocalDate.now().atStartOfDay()
        );

        int year = tx.getTimestamp().getYear();
        int month = tx.getTimestamp().getMonthValue();

        if (tx.getType().equals("EXPENSE") && financeService.isOverBudget(userId, year, month)) {
            System.out.println("[!] Warning: You’ve exceeded your budget for " + year + "-" + month + "!");
        }

        boolean success = financeService.addTransaction(tx);
        System.out.println(success ? "[OK] Transaction added!" : "[X] Failed to add transaction.");
    }

    
    public void promptEditTransaction(int userId) {
        int txId = ConsoleInput.readInt("Enter Transaction ID to edit: ");
        double newAmount = ConsoleInput.readDouble("New amount: ");
        String newNote = ConsoleInput.readString("New note: ");

        boolean success = financeService.updateTransactionAmountAndNote(txId, newAmount, newNote);
        System.out.println(success ? "[OK] Updated successfully." :  "[!] Transaction not found or failed.");
    }

    public void promptDeleteTransaction(int userId) {
        int txId = ConsoleInput.readInt("Enter Transaction ID to delete: ");
        boolean confirmed = ConsoleInput.readString("Are you sure (Y/N)? ").equalsIgnoreCase("Y");

        if (confirmed) {
            boolean success = financeService.deleteTransaction(txId);
            System.out.println(success ? "[OK]️ Deleted!" : "[!]️ Could not delete. Try again.");
        }
    }
    
    public void manageAccounts(int userId) {
        while (true) {
            System.out.println("\n=== Account Management ===");
            System.out.println("1. Create New Account");
            System.out.println("2. View My Accounts");
            System.out.println("3. Delete Account");
            System.out.println("4. Back");

            int choice = ConsoleInput.readInt("Choose: ");
            switch (choice) {
                case 1 -> createAccount(userId);
                case 2 -> showAccounts(userId);
                case 3 -> deleteAccount(userId);
                case 4 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void createAccount(int userId) {
        String name = ConsoleInput.readString("Account name: ");
        double balance = ConsoleInput.readDouble("Opening balance: ");
        boolean success = financeService.createAccount(userId, name, balance);
        System.out.println(success ? "[OK] Account created!" : "[ERROR] Failed to create account.");
    }

    private void showAccounts(int userId) {
        var accounts = financeService.getAccountsByUser(userId);
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] { "ID", "Name", "Balance" });
        for (var acc : accounts) {
            rows.add(new String[] {
                String.valueOf(acc.getAccountId()),
                acc.getName(),
                String.format("%.2f", acc.getBalance())
            });
        }
        TablePrinter.printGrid(rows, new int[] { 5, 20, 12 });
    }


    private void deleteAccount(int userId) {
        int accountId = ConsoleInput.readInt("Enter Account ID to delete: ");
        boolean confirm = ConsoleInput.readString("Are you sure (Y/N)? ").equalsIgnoreCase("Y");
        if (confirm) {
            boolean success = financeService.deleteAccount(accountId);
            System.out.println(success ? "[OK]️ Deleted." : "[!] Could not delete account.");
        }
    }

    public void manageCategories(int userId) {
        while (true) {
            System.out.println("\n=== Category Management ===");
            System.out.println("1. Create Category");
            System.out.println("2. View Categories");
            System.out.println("3. Delete Category");
            System.out.println("4. Back");

            int choice = ConsoleInput.readInt("Choose an option: ");
            switch (choice) {
                case 1 -> createCategory(userId);
                case 2 -> viewCategories(userId);
                case 3 -> deleteCategory(userId);
                case 4 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void createCategory(int userId) {
        String name = ConsoleInput.readString("Enter category name: ");
        boolean success = financeService.createCategory(userId, name);
        System.out.println(success ? "[OK] Category added!" : "[!] Failed to add category.");
    }

    private void viewCategories(int userId) {
        var categories = financeService.getCategoriesByUser(userId);
        if (categories.isEmpty()) {
            System.out.println("No categories created yet.");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] { "ID", "Category Name" });
        for (var cat : categories) {
            rows.add(new String[] {
                String.valueOf(cat.getCategoryId()),
                cat.getName()
            });
        }
        TablePrinter.printGrid(rows, new int[] { 5, 20 });
    }


    private void deleteCategory(int userId) {
        int categoryId = ConsoleInput.readInt("Enter Category ID to delete: ");
        boolean confirm = ConsoleInput.readString("Are you sure (Y/N)? ").equalsIgnoreCase("Y");
        if (confirm) {
            boolean success = financeService.deleteCategory(categoryId);
            System.out.println(success ? "[OK]️ Category deleted." : "[!]️ Unable to delete.");
        }
    }

    public void manageBudget(int userId) {
        int year = ConsoleInput.readInt("Set budget for year: ");
        int month = ConsoleInput.readInt("Set budget for month (1-12): ");
        double amount = ConsoleInput.readDouble("Enter monthly budget (₹): ");

        boolean success = financeService.setMonthlyBudget(userId, year, month, amount);
        System.out.println(success ? "[OK] Budget set!" : "[!] Failed to update.");
    }

    public void checkBudgetStatus(int userId) {
        int year = ConsoleInput.readInt("Check budget for year: ");
        int month = ConsoleInput.readInt("Check for month (1-12): ");

        Double budget = financeService.getMonthlyBudget(userId, year, month);
        if (budget == null) {
            System.out.println("[!] No budget set for this month.");
            return;
        }

        double spent = financeService.getMonthlyCategoryBreakdown(userId, year, month)
                        .values().stream().mapToDouble(Double::doubleValue).sum();

        String status = spent > budget ? "[X]️ Over budget!" : "[OK] Within budget.";
        System.out.printf("\n Budget: Rs.%.2f | Spent: Rs.%.2f -> %s\n", budget, spent, status);
    }

        public ExportService exportService; 
        
        public void exportAllTx(int userId) {
            boolean success = exportService.exportAllTransactions(userId);
            System.out.println(success ? "Exported to 'exports/transactions_*.csv'" : "No transactions found.");
        }

        public void exportSummary(int userId) {
            int year = ConsoleInput.readInt("Year: ");
            int month = ConsoleInput.readInt("Month (1-12): ");
            boolean success = exportService.exportMonthlySummary(userId, year, month);
            System.out.println(success ? "[OK] Summary exported!" : "[!]️ No data for that month.");
        }
}

