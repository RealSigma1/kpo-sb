package financialaccounting.Patterns.Factory;

import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Category;
import financialaccounting.Models.Operation;
import java.util.Date;

public class FinancialFactory {
    public static BankAccount createBankAccount(String name, double initialBalance) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Название счета не может быть пустым");

        if (initialBalance < 0)
            throw new IllegalArgumentException("Начальный баланс не может быть отрицательным");

        return new BankAccount(0, name, initialBalance);
    }

    public static Category createCategory(Category.CategoryType type, String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Название категории не может быть пустым");

        return new Category(0, type, name);
    }

    public static Operation createOperation(Operation.OperationType type, int bankAccountId, double amount,
                                            Date date, String description, int categoryId) {
        if (amount <= 0)
            throw new IllegalArgumentException("Сумма операции должна быть положительной");

        if (bankAccountId <= 0)
            throw new IllegalArgumentException("Неверный идентификатор счета");

        if (categoryId <= 0)
            throw new IllegalArgumentException("Неверный идентификатор категории");

        return new Operation(0, type, bankAccountId, amount, date, description != null ? description : "", categoryId);
    }
}