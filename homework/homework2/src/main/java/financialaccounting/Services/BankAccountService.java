package financialaccounting.Services;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Operation;
import financialaccounting.Patterns.Factory.FinancialFactory;

import java.util.List;

public class BankAccountService {

    private final IBankAccountRepository repository;

    public BankAccountService(IBankAccountRepository repository) {
        this.repository = repository;
    }

    public BankAccount createAccount(String name, double initialBalance) {
        BankAccount account = FinancialFactory.createBankAccount(name, initialBalance);
        repository.add(account);
        System.out.println("Создан счет: " + account);
        return account;
    }

    public BankAccount updateAccount(int id, String newName, double newBalance) {
        BankAccount account = repository.getById(id);
        if (account == null) {
            throw new IllegalArgumentException("Счет с id " + id + " не найден");
        }

        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Название счета не может быть пустым");
        }
        if (newBalance < 0) {
            throw new IllegalArgumentException("Баланс не может быть отрицательным");
        }

        account.setName(newName);
        account.setBalance(newBalance);
        repository.update(account);

        System.out.println("Обновлен счет: " + account);
        return account;
    }

    public void deleteAccount(int id) {
        BankAccount account = repository.getById(id);
        if (account == null) {
            System.out.println("Счет с id " + id + " не найден, удаление пропущено.");
            return;
        }

        repository.delete(id);
        System.out.println("Удален счет: " + account);
    }

    public BankAccount getAccountById(int id) {
        return repository.getById(id);
    }

    public List<BankAccount> getAllAccounts() {
        return repository.getAll();
    }

    public void printAllAccounts() {
        getAllAccounts().forEach(System.out::println);
    }

    public void recalculateBalance(int accountId, IOperationRepository operationRepository) {
        BankAccount account = repository.getById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Счет с id " + accountId + " не найден");
        }

        List<Operation> operations = operationRepository.getByAccountId(accountId);
        double newBalance = operations.stream()
                .mapToDouble(op -> op.getType() == Operation.OperationType.INCOME
                        ? op.getAmount()
                        : -op.getAmount())
                .sum();

        account.setBalance(newBalance);
        repository.update(account);

        System.out.println("Баланс счета пересчитан: " + account);
    }

    public boolean verifyBalance(int accountId, IOperationRepository operationRepository) {
        BankAccount account = repository.getById(accountId);
        if (account == null) {
            return false;
        }

        List<Operation> operations = operationRepository.getByAccountId(accountId);
        double calculatedBalance = operations.stream()
                .mapToDouble(op -> op.getType() == Operation.OperationType.INCOME
                        ? op.getAmount()
                        : -op.getAmount())
                .sum();

        return Math.abs(account.getBalance() - calculatedBalance) < 0.01;
    }
}
