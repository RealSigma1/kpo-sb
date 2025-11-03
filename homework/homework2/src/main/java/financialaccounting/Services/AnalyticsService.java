package financialaccounting.Services;

import financialaccounting.Interfaces.ICategoryRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Patterns.Strategy.IAnalyticsStrategy;
import java.util.Date;

public class AnalyticsService {
    private IOperationRepository operationRepository;
    private ICategoryRepository categoryRepository;

    public AnalyticsService(IOperationRepository operationRepository, ICategoryRepository categoryRepository) {
        this.operationRepository = operationRepository;
        this.categoryRepository = categoryRepository;
    }

    public String getBalanceAnalytics(Date startDate, Date endDate) {
        IAnalyticsStrategy strategy = new financialaccounting.Patterns.Strategy.BalanceAnalytics(startDate, endDate);
        return strategy.analyze(operationRepository.getAll(), categoryRepository.getAll());
    }

    public String getCategoryAnalytics(Date startDate, Date endDate) {
        IAnalyticsStrategy strategy = new financialaccounting.Patterns.Strategy.CategoryAnalytics(startDate, endDate);
        return strategy.analyze(operationRepository.getAll(), categoryRepository.getAll());
    }

    public String getCustomAnalytics(IAnalyticsStrategy strategy) {
        return strategy.analyze(operationRepository.getAll(), categoryRepository.getAll());
    }
}