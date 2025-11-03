package financialaccounting.Patterns.Command;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Operation;
import financialaccounting.Patterns.Factory.FinancialFactory;
import java.util.Date;

public class CreateOperationCommand implements ICommand {
    private IOperationRepository operationRepository;
    private IBankAccountRepository accountRepository;
    private Operation.OperationType type;
    private int accountId;
    private double amount;
    private Date date;
    private String description;
    private int categoryId;
    private Operation createdOperation;
    private BankAccount originalAccount;

    public CreateOperationCommand(IOperationRepository operationRepository,
                                  IBankAccountRepository accountRepository, Operation.OperationType type, int accountId,
                                  double amount, Date date, String description, int categoryId) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
        this.type = type;
        this.accountId = accountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
    }

    @Override
    public void execute() {
        createdOperation = FinancialFactory.createOperation(type, accountId, amount, date, description, categoryId);
        operationRepository.add(createdOperation);

        originalAccount = accountRepository.getById(accountId);
        if (originalAccount != null) {
            double newBalance = type == Operation.OperationType.INCOME
                    ? originalAccount.getBalance() + amount
                    : originalAccount.getBalance() - amount;
            BankAccount updatedAccount = new BankAccount(originalAccount.getId(),
                    originalAccount.getName(), newBalance);
            accountRepository.update(updatedAccount);
        }

        System.out.println("Создана операция: " + createdOperation);
    }

    @Override
    public void undo() {
        if (createdOperation != null) {
            operationRepository.delete(createdOperation.getId());

            if (originalAccount != null) {
                accountRepository.update(originalAccount);
            }

            System.out.println("Отменено создание операции: " + createdOperation.getDescription());
        }
    }
}