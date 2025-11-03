package financialaccounting.Patterns.Proxy;

import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Models.Operation;
import java.util.*;

public class OperationRepositoryProxy implements IOperationRepository {
    private IOperationRepository repository;
    private Map<Integer, Operation> cache = new HashMap<>();

    public OperationRepositoryProxy(IOperationRepository repository) {
        this.repository = repository;
        initializeCache();
    }

    private void initializeCache() {
        for (Operation entity : repository.getAll()) {
            cache.put(entity.getId(), entity);
        }
    }

    @Override
    public void add(Operation entity) {
        repository.add(entity);
        cache.put(entity.getId(), entity);
    }

    @Override
    public void update(Operation entity) {
        repository.update(entity);
        cache.put(entity.getId(), entity);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
        cache.remove(id);
    }

    @Override
    public Operation getById(int id) {
        if (cache.containsKey(id))
            return cache.get(id);

        Operation entity = repository.getById(id);
        if (entity != null)
            cache.put(id, entity);

        return entity;
    }

    @Override
    public List<Operation> getAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public boolean exists(int id) {
        return cache.containsKey(id) || repository.exists(id);
    }

    @Override
    public List<Operation> getByAccountId(int accountId) {
        return repository.getByAccountId(accountId);
    }

    @Override
    public List<Operation> getByPeriod(Date startDate, Date endDate) {
        return repository.getByPeriod(startDate, endDate);
    }

    @Override
    public List<Operation> getByCategoryId(int categoryId) {
        return repository.getByCategoryId(categoryId);
    }
}