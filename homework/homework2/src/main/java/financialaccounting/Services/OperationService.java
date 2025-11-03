package financialaccounting.Services;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Operation;
import financialaccounting.Patterns.Factory.FinancialFactory;
import java.util.Date;
import java.util.List;

public class OperationService {
    public IOperationRepository operationRepository;
    public IBankAccountRepository accountRepository;

    public OperationService(IOperationRepository operationRepository, IBankAccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
    }

    public Operation createOperation(Operation.OperationType type, int accountId, double amount,
                                     Date date, String description, int categoryId) {
        Operation operation = FinancialFactory.createOperation(type, accountId, amount, date, description, categoryId);
        operationRepository.add(operation);

        // Обновляем баланс счета
        updateAccountBalance(accountId, type, amount);

        return operation;
    }

    private void updateAccountBalance(int accountId, Operation.OperationType type, double amount) {
        BankAccount account = accountRepository.getById(accountId);
        if (account != null) {
            double newBalance = type == Operation.OperationType.INCOME
                    ? account.getBalance() + amount
                    : account.getBalance() - amount;

            BankAccount updatedAccount = new BankAccount(account.getId(), account.getName(), newBalance);
            accountRepository.update(updatedAccount);
        }
    }

    public void deleteOperation(int id) {
        Operation operation = operationRepository.getById(id);
        if (operation != null) {
            // Восстанавливаем баланс счета
            BankAccount account = accountRepository.getById(operation.getBankAccountId());
            if (account != null) {
                double newBalance = operation.getType() == Operation.OperationType.INCOME
                        ? account.getBalance() - operation.getAmount()
                        : account.getBalance() + operation.getAmount();

                BankAccount updatedAccount = new BankAccount(account.getId(), account.getName(), newBalance);
                accountRepository.update(updatedAccount);
            }

            operationRepository.delete(id);
        }
    }

    public Operation getOperation(int id) {
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
}