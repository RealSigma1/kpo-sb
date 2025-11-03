package financialaccounting.Services;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Operation;
import financialaccounting.Patterns.Factory.FinancialFactory;

import java.util.Date;
import java.util.List;

public class OperationService {

    public final IOperationRepository operationRepository;
    public final IBankAccountRepository accountRepository;

    public OperationService(IOperationRepository operationRepository, IBankAccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
    }

    public Operation createOperation(Operation.OperationType type,
                                     int bankAccountId,
                                     double amount,
                                     Date date,
                                     String description,
                                     int categoryId) {
        BankAccount account = accountRepository.getById(bankAccountId);
        if (account == null) {
            throw new IllegalArgumentException("Счет с id " + bankAccountId + " не найден");
        }

        Operation operation = FinancialFactory.createOperation(type, bankAccountId, amount, date, description, categoryId);
        operationRepository.add(operation);

        applyToAccountBalance(account, type, amount);
        accountRepository.update(account);

        System.out.println("Создана операция: " + operation);
        return operation;
    }

    public Operation updateOperation(int id,
                                     Operation.OperationType newType,
                                     int newBankAccountId,
                                     double newAmount,
                                     Date newDate,
                                     String newDescription,
                                     int newCategoryId) {
        Operation existing = operationRepository.getById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Операция с id " + id + " не найдена");
        }

        BankAccount oldAccount = accountRepository.getById(existing.getBankAccountId());
        if (oldAccount != null) {
            revertFromAccountBalance(oldAccount, existing.getType(), existing.getAmount());
            accountRepository.update(oldAccount);
        }

        existing.setType(newType);
        existing.setBankAccountId(newBankAccountId);
        existing.setAmount(newAmount);
        existing.setDate(newDate);
        existing.setDescription(newDescription);
        existing.setCategoryId(newCategoryId);

        operationRepository.update(existing);

        BankAccount newAccount = accountRepository.getById(newBankAccountId);
        if (newAccount == null) {
            throw new IllegalArgumentException("Счет с id " + newBankAccountId + " не найден");
        }
        applyToAccountBalance(newAccount, newType, newAmount);
        accountRepository.update(newAccount);

        System.out.println("Обновлена операция: " + existing);
        return existing;
    }

    public void deleteOperation(int id) {
        Operation operation = operationRepository.getById(id);
        if (operation == null) {
            System.out.println("Операция с id " + id + " не найдена, удаление пропущено.");
            return;
        }

        BankAccount account = accountRepository.getById(operation.getBankAccountId());
        if (account != null) {
            revertFromAccountBalance(account, operation.getType(), operation.getAmount());
            accountRepository.update(account);
        }

        operationRepository.delete(id);
        System.out.println("Удалена операция: " + operation);
    }

    public Operation getOperationById(int id) {
        return operationRepository.getById(id);
    }

    public List<Operation> getAllOperations() {
        return operationRepository.getAll();
    }

    public List<Operation> getOperationsByPeriod(Date startDate, Date endDate) {
        return operationRepository.getByPeriod(startDate, endDate);
    }

    public List<Operation> getOperationsByCategory(int categoryId) {
        return operationRepository.getByCategoryId(categoryId);
    }

    private void applyToAccountBalance(BankAccount account, Operation.OperationType type, double amount) {
        if (type == Operation.OperationType.INCOME) {
            account.setBalance(account.getBalance() + amount);
        } else {
            account.setBalance(account.getBalance() - amount);
        }
    }

    private void revertFromAccountBalance(BankAccount account, Operation.OperationType type, double amount) {
        if (type == Operation.OperationType.INCOME) {
            account.setBalance(account.getBalance() - amount);
        } else {
            account.setBalance(account.getBalance() + amount);
        }
    }
}
