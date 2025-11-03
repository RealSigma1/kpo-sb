package financialaccounting.Patterns.Strategy;

import financialaccounting.Models.Category;
import financialaccounting.Models.Operation;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

public class CategoryAnalytics implements IAnalyticsStrategy {
    private Date startDate;
    private Date endDate;

    public CategoryAnalytics(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String analyze(List<Operation> operations, List<Category> categories) {
        List<Operation> periodOperations = operations.stream()
                .filter(o -> !o.getDate().before(startDate) && !o.getDate().after(endDate))
                .toList();

        StringBuilder result = new StringBuilder();
        result.append(String.format("Аналитика по категориям за период %s - %s:\n", startDate, endDate));

        var incomeByCategory = periodOperations.stream()
                .filter(o -> o.getType() == Operation.OperationType.INCOME)
                .collect(Collectors.groupingBy(Operation::getCategoryId,
                        Collectors.summingDouble(Operation::getAmount)));

        result.append("\nДоходы по категориям:\n");
        incomeByCategory.forEach((categoryId, total) -> {
            String categoryName = categories.stream()
                    .filter(c -> c.getId() == categoryId)
                    .findFirst()
                    .map(Category::getName)
                    .orElse("Неизвестно");
            result.append(String.format("  %s: %.2f\n", categoryName, total));
        });

        var expenseByCategory = periodOperations.stream()
                .filter(o -> o.getType() == Operation.OperationType.EXPENSE)
                .collect(Collectors.groupingBy(Operation::getCategoryId,
                        Collectors.summingDouble(Operation::getAmount)));

        result.append("\nРасходы по категориям:\n");
        expenseByCategory.forEach((categoryId, total) -> {
            String categoryName = categories.stream()
                    .filter(c -> c.getId() == categoryId)
                    .findFirst()
                    .map(Category::getName)
                    .orElse("Неизвестно");
            result.append(String.format("  %s: %.2f\n", categoryName, total));
        });

        return result.toString();
    }
}
