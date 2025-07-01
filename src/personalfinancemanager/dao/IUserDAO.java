package personalfinancemanager.dao;
import personalfinancemanager.models.User;

public interface IUserDAO {
    User findByUsername(String username);
    boolean save(User user);
}