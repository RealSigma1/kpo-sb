package financialaccounting.Services;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Operation;
import financialaccounting.Patterns.Factory.FinancialFactory;
import java.util.List;

public class BankAccountService {
    public IBankAccountRepository repository;

    public BankAccountService(IBankAccountRepository repository) {
        this.repository = repository;
    }

    public BankAccount createAccount(String name, double initialBalance) {
        BankAccount account = FinancialFactory.createBankAccount(name, initialBalance);
        repository.add(account);
        return account;
    }

    public List<BankAccount> getAllAccounts() {
        return repository.getAll();
    }

    // Метод для пересчета баланса
    public void recalculateBalance(int accountId, IOperationRepository operationRepository) {
        BankAccount account = repository.getById(accountId);
        if (account == null) {
            System.out.println("Счет с ID " + accountId + " не найден");
            return;
        }

        List<Operation> operations = operationRepository.getByAccountId(accountId);
        double newBalance = operations.stream()
                .mapToDouble(op -> op.getType() == Operation.OperationType.INCOME ? op.getAmount() : -op.getAmount())
                .sum();

        BankAccount updatedAccount = new BankAccount(account.getId(), account.getName(), newBalance);
        repository.update(updatedAccount);
        System.out.println("Баланс счета '" + account.getName() + "' пересчитан: " + newBalance);
    }

    // Метод для проверки корректности баланса
    public boolean verifyBalance(int accountId, IOperationRepository operationRepository) {
        BankAccount account = repository.getById(accountId);
        if (account == null) return false;

        List<Operation> operations = operationRepository.getByAccountId(accountId);
        double calculatedBalance = operations.stream()
                .mapToDouble(op -> op.getType() == Operation.OperationType.INCOME ? op.getAmount() : -op.getAmount())
                .sum();

        return Math.abs(account.getBalance() - calculatedBalance) < 0.01;
    }
}