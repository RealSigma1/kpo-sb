package financialaccounting.Repositories;

import financialaccounting.Interfaces.IRepository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

public abstract class BaseRepository<T> implements IRepository<T> {
    protected ConcurrentHashMap<Integer, T> entities = new ConcurrentHashMap<>();
    protected int nextId = 1;

    @Override
    public void add(T entity) {
        try {
            var idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, nextId);
            entities.put(nextId, entity);
            nextId++;
        } catch (Exception e) {
            throw new RuntimeException("Error setting id for entity", e);
        }
    }

    @Override
    public void update(T entity) {
        try {
            var idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            int id = (int) idField.get(entity);
            if (entities.containsKey(id)) {
                entities.put(id, entity);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating entity", e);
        }
    }

    @Override
    public void delete(int id) {
        entities.remove(id);
    }

    @Override
    public T getById(int id) {
        return entities.get(id);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public boolean exists(int id) {
        return entities.containsKey(id);
    }
}