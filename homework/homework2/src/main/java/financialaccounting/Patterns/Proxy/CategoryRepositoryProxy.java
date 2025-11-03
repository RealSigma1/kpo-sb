package financialaccounting.Patterns.Proxy;

import financialaccounting.Interfaces.ICategoryRepository;
import financialaccounting.Models.Category;
import java.util.*;

public class CategoryRepositoryProxy implements ICategoryRepository {
    private ICategoryRepository repository;
    private Map<Integer, Category> cache = new HashMap<>();

    public CategoryRepositoryProxy(ICategoryRepository repository) {
        this.repository = repository;
        initializeCache();
    }

    private void initializeCache() {
        for (Category entity : repository.getAll()) {
            cache.put(entity.getId(), entity);
        }
    }

    @Override
    public void add(Category entity) {
        repository.add(entity);
        cache.put(entity.getId(), entity);
    }

    @Override
    public void update(Category entity) {
        repository.update(entity);
        cache.put(entity.getId(), entity);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
        cache.remove(id);
    }

    @Override
    public Category getById(int id) {
        if (cache.containsKey(id))
            return cache.get(id);

        Category entity = repository.getById(id);
        if (entity != null)
            cache.put(id, entity);

        return entity;
    }

    @Override
    public List<Category> getAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public boolean exists(int id) {
        return cache.containsKey(id) || repository.exists(id);
    }
}