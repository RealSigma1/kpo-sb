package financialaccounting.Services;

import financialaccounting.Interfaces.ICategoryRepository;
import financialaccounting.Models.Category;
import financialaccounting.Patterns.Factory.FinancialFactory;

import java.util.List;

public class CategoryService {

    private final ICategoryRepository repository;

    public CategoryService(ICategoryRepository repository) {
        this.repository = repository;
    }

    public Category createCategory(Category.CategoryType type, String name) {
        Category category = FinancialFactory.createCategory(type, name);
        repository.add(category);
        System.out.println("Создана категория: " + category);
        return category;
    }

    public Category updateCategory(int id, Category.CategoryType newType, String newName) {
        Category category = repository.getById(id);
        if (category == null) {
            throw new IllegalArgumentException("Категория с id " + id + " не найдена");
        }

        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }

        category.setType(newType);
        category.setName(newName);
        repository.update(category);

        System.out.println("Обновлена категория: " + category);
        return category;
    }

    public void deleteCategory(int id) {
        Category category = repository.getById(id);
        if (category == null) {
            System.out.println("Категория с id " + id + " не найдена, удаление пропущено.");
            return;
        }

        repository.delete(id);
        System.out.println("Удалена категория: " + category);
    }

    public Category getCategoryById(int id) {
        return repository.getById(id);
    }

    public List<Category> getAllCategories() {
        return repository.getAll();
    }
}
