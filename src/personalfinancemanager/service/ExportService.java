package personalfinancemanager.service;

import personalfinancemanager.models.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ExportService {
    private final FinanceService financeService;

    public ExportService(FinanceService financeService) {
        this.financeService = financeService;
    }

    public boolean exportAllTransactions(int userId) {
        List<Transaction> txList = financeService.getAllTransactions(userId);
        if (txList.isEmpty()) return false;

        String fileName = generateFilename("transactions");
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("TransactionID,Type,Amount,CategoryID,Timestamp,Note\n");
            for (Transaction tx : txList) {
                writer.write(String.format("%d,%s,%.2f,%d,%s,%s\n",
                        tx.getTransactionId(),
                        tx.getType(),
                        tx.getAmount(),
                        tx.getCategoryId(),
                        tx.getTimestamp(),
                        tx.getNote() != null ? tx.getNote() : ""));
            }
            return true;
        } catch (IOException e) {
            System.err.println("Export failed: " + e.getMessage());
            return false;
        }
    }

    public boolean exportMonthlySummary(int userId, int year, int month) {
        Map<String, Double> breakdown = financeService.getMonthlyCategoryBreakdown(userId, year, month);
        Double budget = financeService.getMonthlyBudget(userId, year, month);

        if (breakdown.isEmpty()) return false;

        String fileName = generateFilename("summary_" + year + "_" + month);
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Category,Amount\n");
            for (var entry : breakdown.entrySet()) {
                writer.write(entry.getKey() + "," + String.format("%.2f", entry.getValue()) + "\n");
            }
            if (budget != null) {
                writer.write("\nTotal Budget," + budget + "\n");
                double spent = breakdown.values().stream().mapToDouble(Double::doubleValue).sum();
                writer.write("Total Spent," + spent + "\n");
                writer.write("Remaining," + (budget - spent) + "\n");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Export failed: " + e.getMessage());
            return false;
        }
    }

    private String generateFilename(String prefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "exports/" + prefix + "_" + timestamp + ".csv";
    }
}
