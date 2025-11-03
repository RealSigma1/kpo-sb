package financialaccounting.Patterns.Strategy;

import financialaccounting.Models.Category;
import financialaccounting.Models.Operation;
import java.util.List;
import java.util.Date;

public class BalanceAnalytics implements IAnalyticsStrategy {
    private Date startDate;
    private Date endDate;

    public BalanceAnalytics(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String analyze(List<Operation> operations, List<Category> categories) {
        List<Operation> periodOperations = operations.stream()
                .filter(o -> !o.getDate().before(startDate) && !o.getDate().after(endDate))
                .toList();

        double totalIncome = periodOperations.stream()
                .filter(o -> o.getType() == Operation.OperationType.INCOME)
                .mapToDouble(Operation::getAmount)
                .sum();

        double totalExpense = periodOperations.stream()
                .filter(o -> o.getType() == Operation.OperationType.EXPENSE)
                .mapToDouble(Operation::getAmount)
                .sum();

        double balance = totalIncome - totalExpense;

        return String.format("Аналитика за период %s - %s:\nДоходы: %.2f\nРасходы: %.2f\nБаланс: %.2f",
                startDate, endDate, totalIncome, totalExpense, balance);
    }
}