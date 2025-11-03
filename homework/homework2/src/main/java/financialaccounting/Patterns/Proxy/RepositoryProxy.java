package financialaccounting.Patterns.Proxy;

import financialaccounting.Interfaces.IRepository;
import java.util.*;

class RepositoryProxy<T> implements IRepository<T> {
    private IRepository<T> repository;
    private Map<Integer, T> cache = new HashMap<>();

    public RepositoryProxy(IRepository<T> repository) {
        this.repository = repository;
        initializeCache();
    }

    private void initializeCache() {
        for (T entity : repository.getAll()) {
            try {
                var field = entity.getClass().getDeclaredField("id");
                field.setAccessible(true);
                int id = (int) field.get(entity);
                cache.put(id, entity);
            } catch (Exception e) {
                throw new RuntimeException("Error initializing cache", e);
            }
        }
    }

    @Override
    public void add(T entity) {
        repository.add(entity);
        try {
            var field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            int id = (int) field.get(entity);
            cache.put(id, entity);
        } catch (Exception e) {
            throw new RuntimeException("Error adding to cache", e);
        }
    }

    @Override
    public void update(T entity) {
        repository.update(entity);
        try {
            var field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            int id = (int) field.get(entity);
            cache.put(id, entity);
        } catch (Exception e) {
            throw new RuntimeException("Error updating cache", e);
        }
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
        cache.remove(id);
    }

    @Override
    public T getById(int id) {
        if (cache.containsKey(id))
            return cache.get(id);

        T entity = repository.getById(id);
        if (entity != null)
            cache.put(id, entity);

        return entity;
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public boolean exists(int id) {
        return cache.containsKey(id) || repository.exists(id);
    }
}