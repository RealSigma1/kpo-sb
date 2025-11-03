package financialaccounting.Patterns.Command;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Models.BankAccount;
import financialaccounting.Patterns.Factory.FinancialFactory;

public class CreateAccountCommand implements ICommand {
    private IBankAccountRepository repository;
    private String name;
    private double initialBalance;
    private BankAccount createdAccount;

    public CreateAccountCommand(IBankAccountRepository repository, String name, double initialBalance) {
        this.repository = repository;
        this.name = name;
        this.initialBalance = initialBalance;
    }

    @Override
    public void execute() {
        createdAccount = FinancialFactory.createBankAccount(name, initialBalance);
        repository.add(createdAccount);
        System.out.println("Создан счет: " + createdAccount);
    }

    @Override
    public void undo() {
        if (createdAccount != null) {
            repository.delete(createdAccount.getId());
            System.out.println("Отменено создание счета: " + createdAccount.getName());
        }
    }
}