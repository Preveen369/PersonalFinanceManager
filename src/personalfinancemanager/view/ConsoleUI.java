package personalfinancemanager.view;

import personalfinancemanager.auth.AuthManager;
import personalfinancemanager.auth.Session;
import personalfinancemanager.models.User;
import personalfinancemanager.service.FinanceService;
import personalfinancemanager.util.ConsoleInput;

public class ConsoleUI {
    private final AuthManager authManager;
    private final FinanceService financeService;
    private final ReportView reportView;

    public ConsoleUI() {
        this.authManager = new AuthManager();
        this.financeService = new FinanceService();
        this.reportView = new ReportView(financeService);
    }

    public void run() {
        while (true) {
            System.out.println("\n=== Personal Finance Manager ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            int choice = ConsoleInput.readInt("Choose an option: ");

            switch (choice) {
                case 1 -> {
                    User user = authManager.login();
                    if (user != null) {
                        Session.setUser(user);
                        dashboard();
                    }
                }
                case 2 -> {
                    if (authManager.register()) {
                        System.out.println("[OK] Registration successful. Please login.");
                    }
                }
                case 3 -> {
                    System.out.println("[Exit] Goodbye!");
                    return;
                }
                default -> System.out.println("[!] Invalid choice.");
            }
        }
    }

    private void dashboard() {
        int userId = Session.getUser().getUserId();

        while (Session.isAuthenticated()) {
            System.out.println("\n=== Dashboard ===");
            System.out.println("1. Add Transaction");
            System.out.println("2. Edit Transaction");
            System.out.println("3. Delete Transaction");
            System.out.println("4. View All Transactions");
            System.out.println("5. View Monthly Category Report");
            System.out.println("6. View Top Spending Categories");
            System.out.println("7. View Net Savings");
            System.out.println("8. Set Monthly Budget");
            System.out.println("9. Check Budget Status");
            System.out.println("10. Manage Accounts");
            System.out.println("11. Manage Categories");
            System.out.println("12. Export All Transactions to CSV");
            System.out.println("13. Export Monthly Summary");
            System.out.println("14. Logout");

            int choice = ConsoleInput.readInt("Select an option: ");

            switch (choice) {
                case 1 -> reportView.promptNewTransaction(userId);
                case 2 -> reportView.promptEditTransaction(userId);
                case 3 -> reportView.promptDeleteTransaction(userId);
                case 4 -> reportView.printAllTransactions(userId);
                case 5 -> reportView.printMonthlyBreakdown(userId);
                case 6 -> reportView.printTopSpendingCategories(userId);
                case 7 -> reportView.printSavings(userId);
                case 8 -> reportView.manageBudget(userId);
                case 9 -> reportView.checkBudgetStatus(userId);
                case 10 -> reportView.manageAccounts(userId);
                case 11 -> reportView.manageCategories(userId);
                case 12 -> reportView.exportAllTx(userId);
                case 13 -> reportView.exportSummary(userId);
                case 14 -> {
                    Session.logout();
                    System.out.println("[Logout] You have been logged out.");
                }
                default -> System.out.println("[!] Invalid option.");
            }
        }
    }
}
