package personalfinancemanager.dao;

import personalfinancemanager.models.Category;
import java.util.List;

public interface ICategoryDAO {
    boolean save(Category category);
    List<Category> findAllByUserId(int userId);
    int findUserByCategoryId(int categoryId);
    Category findByName(int userId, String name);
    boolean delete(int categoryId);
}