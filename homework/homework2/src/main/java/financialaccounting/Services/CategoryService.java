package financialaccounting.Services;

import financialaccounting.Interfaces.ICategoryRepository;
import financialaccounting.Models.Category;
import financialaccounting.Patterns.Factory.FinancialFactory;
import java.util.List;

public class CategoryService {
    public ICategoryRepository repository;

    public CategoryService(ICategoryRepository repository) {
        this.repository = repository;
    }

    public Category createCategory(Category.CategoryType type, String name) {
        Category category = FinancialFactory.createCategory(type, name);
        repository.add(category);
        System.out.println("Создана категория: " + category); // ← ДОБАВИТЬ ЭТУ СТРОКУ
        return category;
    }

    public List<Category> getAllCategories() {
        return repository.getAll();
    }
}