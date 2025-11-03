package financialaccounting.Services;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Interfaces.ICategoryRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Patterns.Visitor.CsvExportVisitor;
import financialaccounting.Patterns.Visitor.IExportVisitor;
import financialaccounting.Patterns.Visitor.JsonExportVisitor;

public class ExportService {
    private IBankAccountRepository accountRepository;
    private ICategoryRepository categoryRepository;
    private IOperationRepository operationRepository;

    public ExportService(IBankAccountRepository accountRepository,
                         ICategoryRepository categoryRepository,
                         IOperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    public String exportToCsv() {
        IExportVisitor visitor = new CsvExportVisitor();
        exportAllEntities(visitor);
        return visitor.getResult();
    }

    public String exportToJson() {
        IExportVisitor visitor = new JsonExportVisitor();
        exportAllEntities(visitor);
        return visitor.getResult();
    }

    private void exportAllEntities(IExportVisitor visitor) {
        accountRepository.getAll().forEach(visitor::visit);
        categoryRepository.getAll().forEach(visitor::visit);
        operationRepository.getAll().forEach(visitor::visit);
    }
}