package personalfinancemanager.service;

import java.time.LocalDate;
import personalfinancemanager.dao.TransactionDAO;
import personalfinancemanager.dao.CategoryDAO;
import personalfinancemanager.dao.AccountDAO;
import personalfinancemanager.dao.BudgetDAO;
import personalfinancemanager.models.Transaction;
import personalfinancemanager.models.Category;

import java.util.*;
import java.util.stream.Collectors;
import personalfinancemanager.models.Account;

public class FinanceService {
    private final TransactionDAO transactionDAO;
    private final CategoryDAO categoryDAO;
    private final AccountDAO accountDAO;

    public FinanceService() {
        this.transactionDAO = new TransactionDAO();
        this.categoryDAO = new CategoryDAO();
        this.accountDAO = new AccountDAO();
    }
    
    public boolean addTransaction(Transaction transaction) {
        return transactionDAO.save(transaction);
    }
    
    public boolean deleteTransaction(int transactionId) {
        return transactionDAO.delete(transactionId);
    }

    public boolean updateTransactionAmountAndNote(int transactionId, double newAmount, String newNote) {
        return transactionDAO.updateTransaction(transactionId, newAmount, newNote);
    }

    // Fetch all transactions for a user
    public List<Transaction> getAllTransactions(int userId) {
        return transactionDAO.findByUser(userId);
    }

    // Total income or expense
    public double getTotal(String type, int userId) {
        return transactionDAO.getTotalByType(userId, type.toUpperCase());
    }

    public boolean createAccount(int userId, String name, double balance) {
        var acc = new Account(0, userId, name, balance, LocalDate.now().atStartOfDay());
        return accountDAO.save(acc);
    }

    public List<Account> getAccountsByUser(int userId) {
        return accountDAO.findAllByUserId(userId);
    }

    public boolean deleteAccount(int accountId) {
        return accountDAO.delete(accountId);
    }
    
    public boolean createCategory(int userId, String name) {
        var category = new Category(0, userId, name, LocalDate.now().atStartOfDay());
        return categoryDAO.save(category);
    }

    public List<Category> getCategoriesByUser(int userId) {
        return categoryDAO.findAllByUserId(userId);
    }

    public boolean deleteCategory(int categoryId) {
        return categoryDAO.delete(categoryId);
    }


    // Generate net savings
    public double getNetSavings(int userId) {
        double income = getTotal("INCOME", userId);
        double expense = getTotal("EXPENSE", userId);
        return income - expense;
    }

    // Monthly expense summary grouped by category
    public Map<String, Double> getMonthlyCategoryBreakdown(int userId, int year, int month) {
        List<Transaction> txList = transactionDAO.findByMonth(userId, year, month);
        List<Category> userCategories = categoryDAO.findAllByUserId(userId);

        Map<Integer, String> categoryIdNameMap = userCategories.stream()
                .collect(Collectors.toMap(Category::getCategoryId, Category::getName));

        return txList.stream()
                .filter(tx -> "EXPENSE".equalsIgnoreCase(tx.getType()))
                .collect(Collectors.groupingBy(
                        tx -> categoryIdNameMap.getOrDefault(tx.getCategoryId(), "Unknown"),
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    // Top 3 spending categories
    public List<Map.Entry<String, Double>> getTopCategories(int userId, int year, int month) {
        Map<String, Double> breakdown = getMonthlyCategoryBreakdown(userId, year, month);
        return breakdown.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .toList();
    }
    
    private final BudgetDAO budgetDAO = new BudgetDAO();

    public boolean setMonthlyBudget(int userId, int year, int month, double amount) {
        return budgetDAO.saveOrUpdate(userId, year, month, amount);
    }

    public Double getMonthlyBudget(int userId, int year, int month) {
        return budgetDAO.getBudget(userId, year, month);
    }

    public boolean isOverBudget(int userId, int year, int month) {
        double spent = getMonthlyCategoryBreakdown(userId, year, month)
                         .values().stream().mapToDouble(Double::doubleValue).sum();
        Double budget = getMonthlyBudget(userId, year, month);
        return budget != null && spent > budget;
    }

}
