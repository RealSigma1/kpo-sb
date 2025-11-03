package financialaccounting.Interfaces;

import financialaccounting.Models.Category;
import java.util.List;

public interface ICategoryRepository {
    void add(Category entity);
    void update(Category entity);
    void delete(int id);
    Category getById(int id);
    List<Category> getAll();
    boolean exists(int id);
}