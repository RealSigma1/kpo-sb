package financialaccounting.Repositories;

import financialaccounting.Interfaces.ICategoryRepository;
import financialaccounting.Models.Category;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryRepository implements ICategoryRepository {
    protected Map<Integer, Category> entities = new ConcurrentHashMap<>();
    protected int nextId = 1;

    @Override
    public void add(Category entity) {
        entity.setId(nextId);
        entities.put(nextId, entity);
        nextId++;
    }

    @Override
    public void update(Category entity) {
        entities.put(entity.getId(), entity);
    }

    @Override
    public void delete(int id) {
        entities.remove(id);
    }

    @Override
    public Category getById(int id) {
        return entities.get(id);
    }

    @Override
    public List<Category> getAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public boolean exists(int id) {
        return entities.containsKey(id);
    }
}