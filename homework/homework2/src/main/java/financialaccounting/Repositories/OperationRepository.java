package financialaccounting.Repositories;

import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Models.Operation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OperationRepository implements IOperationRepository {
    protected Map<Integer, Operation> entities = new ConcurrentHashMap<>();
    protected int nextId = 1;

    @Override
    public void add(Operation entity) {
        entity.setId(nextId);
        entities.put(nextId, entity);
        nextId++;
    }

    @Override
    public void update(Operation entity) {
        entities.put(entity.getId(), entity);
    }

    @Override
    public void delete(int id) {
        entities.remove(id);
    }

    @Override
    public Operation getById(int id) {
        return entities.get(id);
    }

    @Override
    public List<Operation> getAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public boolean exists(int id) {
        return entities.containsKey(id);
    }

    @Override
    public List<Operation> getByAccountId(int accountId) {
        return entities.values().stream()
                .filter(op -> op.getBankAccountId() == accountId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> getByPeriod(Date startDate, Date endDate) {
        return entities.values().stream()
                .filter(op -> !op.getDate().before(startDate) && !op.getDate().after(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> getByCategoryId(int categoryId) {
        return entities.values().stream()
                .filter(op -> op.getCategoryId() == categoryId)
                .collect(Collectors.toList());
    }
}