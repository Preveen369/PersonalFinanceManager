package personalfinancemanager.dao;

import personalfinancemanager.models.Account;
import java.util.List;

public interface IAccountDAO {
    boolean save(Account account);
    boolean updateBalance(int accountId, double newBalance);
    List<Account> findAllByUserId(int userId);
    Account findById(int accountId);
    boolean delete(int accountId);
}
