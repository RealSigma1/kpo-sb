package financialaccounting.Patterns.Proxy;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Models.BankAccount;
import java.util.*;

public class BankAccountRepositoryProxy implements IBankAccountRepository {
    private IBankAccountRepository repository;
    private Map<Integer, BankAccount> cache = new HashMap<>();

    public BankAccountRepositoryProxy(IBankAccountRepository repository) {
        this.repository = repository;
        initializeCache();
    }

    private void initializeCache() {
        for (BankAccount entity : repository.getAll()) {
            cache.put(entity.getId(), entity);
        }
    }

    @Override
    public void add(BankAccount entity) {
        repository.add(entity);
        cache.put(entity.getId(), entity);
    }

    @Override
    public void update(BankAccount entity) {
        repository.update(entity);
        cache.put(entity.getId(), entity);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
        cache.remove(id);
    }

    @Override
    public BankAccount getById(int id) {
        if (cache.containsKey(id))
            return cache.get(id);

        BankAccount entity = repository.getById(id);
        if (entity != null)
            cache.put(id, entity);

        return entity;
    }

    @Override
    public List<BankAccount> getAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public boolean exists(int id) {
        return cache.containsKey(id) || repository.exists(id);
    }
}