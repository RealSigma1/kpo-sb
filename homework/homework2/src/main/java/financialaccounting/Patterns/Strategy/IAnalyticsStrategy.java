package financialaccounting.Patterns.Strategy;

import financialaccounting.Models.Category;
import financialaccounting.Models.Operation;
import java.util.List;

public interface IAnalyticsStrategy {
    String analyze(List<Operation> operations, List<Category> categories);
}