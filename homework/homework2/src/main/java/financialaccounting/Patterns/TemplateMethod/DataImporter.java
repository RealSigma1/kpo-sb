package financialaccounting.Patterns.TemplateMethod;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Interfaces.ICategoryRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Category;
import financialaccounting.Models.Operation;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class DataImporter {
    protected final IBankAccountRepository accountRepository;
    protected final ICategoryRepository categoryRepository;
    protected final IOperationRepository operationRepository;

    protected DataImporter(IBankAccountRepository accountRepository,
                           ICategoryRepository categoryRepository,
                           IOperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    public final void importData(String filePath) throws IOException {
        System.out.println("Начало импорта из файла: " + filePath);

        if (!new File(filePath).exists()) {
            throw new IOException("Файл не найден: " + filePath);
        }

        String data = readFile(filePath);
        List<Object> entities = parseData(data);
        saveEntities(entities);
        logImportResult(entities);

        System.out.println("Импорт завершен успешно");
    }

    protected String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(new File(filePath).toPath()));
    }

    protected abstract List<Object> parseData(String data);

    protected void saveEntities(List<Object> entities) {
        for (Object entity : entities) {
            if (entity instanceof BankAccount) {
                accountRepository.add((BankAccount) entity);
            } else if (entity instanceof Category) {
                categoryRepository.add((Category) entity);
            } else if (entity instanceof Operation) {
                operationRepository.add((Operation) entity);
            }
        }
    }

    protected void logImportResult(List<Object> entities) {
        long accounts = entities.stream().filter(e -> e instanceof BankAccount).count();
        long categories = entities.stream().filter(e -> e instanceof Category).count();
        long operations = entities.stream().filter(e -> e instanceof Operation).count();

        System.out.printf("Импортировано: %d счетов, %d категорий, %d операций\n", accounts, categories, operations);
    }

    protected BankAccount parseBankAccount(String[] data) {
        int id = Integer.parseInt(data[1]);
        String name = data[2];
        double balance = Double.parseDouble(data[3]);
        return new BankAccount(id, name, balance);
    }

    protected Category parseCategory(String[] data) {
        int id = Integer.parseInt(data[1]);
        Category.CategoryType type = Category.CategoryType.valueOf(data[4]);
        String name = data[5];
        return new Category(id, type, name);
    }

    protected Operation parseOperation(String[] data) throws ParseException {
        int id = Integer.parseInt(data[1]);
        Operation.OperationType type = Operation.OperationType.valueOf(data[6]);
        int accountId = Integer.parseInt(data[7]);
        double amount = Double.parseDouble(data[8]);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = dateFormat.parse(data[9]);
        String description = data[10];
        int categoryId = Integer.parseInt(data[11]);

        return new Operation(id, type, accountId, amount, date, description, categoryId);
    }
}