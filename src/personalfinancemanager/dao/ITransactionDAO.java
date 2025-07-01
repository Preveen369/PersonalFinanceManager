package personalfinancemanager.dao;

import personalfinancemanager.models.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface ITransactionDAO {
    boolean save(Transaction transaction);
    List<Transaction> findByUser(int userId);
    List<Transaction> findByMonth(int userId, int year, int month);
    List<Transaction> findByCategory(int categoryId);
    double getTotalByType(int userId, String type); // INCOME or EXPENSE
    boolean delete(int transactionId);
}
