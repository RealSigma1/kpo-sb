package financialaccounting.Interfaces;

import financialaccounting.Models.BankAccount;
import java.util.List;

public interface IBankAccountRepository {
    void add(BankAccount entity);
    void update(BankAccount entity);
    void delete(int id);
    BankAccount getById(int id);
    List<BankAccount> getAll();
    boolean exists(int id);
}