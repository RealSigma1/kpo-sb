package financialaccounting.Repositories;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Models.BankAccount;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BankAccountRepository implements IBankAccountRepository {
    protected Map<Integer, BankAccount> entities = new ConcurrentHashMap<>();
    protected int nextId = 1;

    @Override
    public void add(BankAccount entity) {
        entity.setId(nextId);
        entities.put(nextId, entity);
        nextId++;
    }

    @Override
    public void update(BankAccount entity) {
        entities.put(entity.getId(), entity);
    }

    @Override
    public void delete(int id) {
        entities.remove(id);
    }

    @Override
    public BankAccount getById(int id) {
        return entities.get(id);
    }

    @Override
    public List<BankAccount> getAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public boolean exists(int id) {
        return entities.containsKey(id);
    }
}