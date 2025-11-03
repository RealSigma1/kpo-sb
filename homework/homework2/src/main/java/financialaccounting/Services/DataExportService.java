package financialaccounting.Services;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Interfaces.ICategoryRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Patterns.Visitor.*;
import java.io.FileWriter;
import java.io.IOException;

public class DataExportService {
    private final IBankAccountRepository accountRepository;
    private final ICategoryRepository categoryRepository;
    private final IOperationRepository operationRepository;

    public DataExportService(IBankAccountRepository accountRepository,
                             ICategoryRepository categoryRepository,
                             IOperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    public void exportToCsv(String filePath) throws IOException {
        IExportVisitor visitor = new CsvExportVisitor();
        exportAllEntities(visitor);
        saveToFile(filePath, visitor.getResult());
        System.out.println("Данные экспортированы в CSV: " + filePath);
    }

    public void exportToJson(String filePath) throws IOException {
        IExportVisitor visitor = new JsonExportVisitor();
        exportAllEntities(visitor);
        saveToFile(filePath, visitor.getResult());
        System.out.println("Данные экспортированы в JSON: " + filePath);
    }

    public void exportToYaml(String filePath) throws IOException {
        IExportVisitor visitor = new YamlExportVisitor();
        exportAllEntities(visitor);
        saveToFile(filePath, visitor.getResult());
        System.out.println("Данные экспортированы в YAML: " + filePath);
    }

    private void exportAllEntities(IExportVisitor visitor) {
        accountRepository.getAll().forEach(visitor::visit);
        categoryRepository.getAll().forEach(visitor::visit);
        operationRepository.getAll().forEach(visitor::visit);
    }

    private void saveToFile(String filePath, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
    }
}