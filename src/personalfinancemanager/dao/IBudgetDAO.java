package personalfinancemanager.dao;

public interface IBudgetDAO {
    boolean saveOrUpdate(int userId, int year, int month, double amount);
    Double getBudget(int userId, int year, int month);
}
