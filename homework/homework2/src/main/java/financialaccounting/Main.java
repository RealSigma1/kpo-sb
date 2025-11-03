package financialaccounting;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Interfaces.ICategoryRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Models.*;
import financialaccounting.Patterns.Command.*;
import financialaccounting.Patterns.Proxy.*;
import financialaccounting.Patterns.TemplateMethod.JsonDataImporter;
import financialaccounting.Repositories.*;
import financialaccounting.Services.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ВШЭ-Банк: Модуль учета финансов ===\n");

        IBankAccountRepository accountRepository = new BankAccountRepositoryProxy(new BankAccountRepository());
        ICategoryRepository categoryRepository = new CategoryRepositoryProxy(new CategoryRepository());
        IOperationRepository operationRepository = new OperationRepositoryProxy(new OperationRepository());

        BankAccountService accountService = new BankAccountService(accountRepository);
        CategoryService categoryService = new CategoryService(categoryRepository);
        OperationService operationService = new OperationService(operationRepository, accountRepository);
        AnalyticsService analyticsService = new AnalyticsService(operationRepository, categoryRepository);
        DataExportService exportService = new DataExportService(accountRepository, categoryRepository, operationRepository);

        demoFunctionality(
                accountService,
                categoryService,
                operationService,
                analyticsService,
                exportService,
                accountRepository,
                categoryRepository,
                operationRepository
        );

        System.out.println("\n=== Завершение ===");
    }

    static void demoFunctionality(BankAccountService accountService,
                                  CategoryService categoryService,
                                  OperationService operationService,
                                  AnalyticsService analyticsService,
                                  DataExportService exportService,
                                  IBankAccountRepository accountRepository,
                                  ICategoryRepository categoryRepository,
                                  IOperationRepository operationRepository) {

        System.out.println("1. Создание счетов:");
        CreateAccountCommand createAccount1 = new CreateAccountCommand(
                accountRepository,
                "Основной счет", 10000
        );
        ICommand timedCommand1 = new CommandTimerDecorator(createAccount1);
        timedCommand1.execute();

        CreateAccountCommand createAccount2 = new CreateAccountCommand(
                accountRepository,
                "Накопительный счет", 50000
        );
        ICommand timedCommand2 = new CommandTimerDecorator(createAccount2);
        timedCommand2.execute();

        System.out.println("\n1.1 Обновление и удаление счетов:");
        List<BankAccount> accounts = accountService.getAllAccounts();
        if (!accounts.isEmpty()) {
            BankAccount first = accounts.get(0);
            accountService.updateAccount(
                    first.getId(),
                    first.getName() + " (обновлен)",
                    first.getBalance() + 1000
            );
        }

        accounts = accountService.getAllAccounts();
        if (accounts.size() > 1) {
            BankAccount second = accounts.get(1);
            accountService.deleteAccount(second.getId());
        }

        System.out.println("Текущие счета после операций:");
        accountService.getAllAccounts().forEach(System.out::println);

        System.out.println("\n2. Создание категорий:");
        Category salaryCategory = categoryService.createCategory(Category.CategoryType.INCOME, "Зарплата");
        Category foodCategory = categoryService.createCategory(Category.CategoryType.EXPENSE, "Продукты");
        Category transportCategory = categoryService.createCategory(Category.CategoryType.EXPENSE, "Транспорт");
        Category entertainmentCategory = categoryService.createCategory(Category.CategoryType.EXPENSE, "Развлечения");

        System.out.println("\n2.1 Обновление и удаление категорий:");
        categoryService.updateCategory(
                foodCategory.getId(),
                foodCategory.getType(),
                "Продукты и супермаркет"
        );

        categoryService.deleteCategory(entertainmentCategory.getId());

        System.out.println("Текущие категории после операций:");
        categoryService.getAllCategories().forEach(System.out::println);

        System.out.println("\n3. Создание операций:");
        int accountId = accountService.getAllAccounts().get(0).getId();

        CreateOperationCommand createOperation1 = new CreateOperationCommand(
                operationRepository,
                accountRepository,
                Operation.OperationType.INCOME, accountId, 50000,
                new Date(System.currentTimeMillis() - 10L * 24 * 60 * 60 * 1000),
                "Зарплата за март", salaryCategory.getId()
        );
        ICommand timedOperation1 = new CommandTimerDecorator(createOperation1);
        timedOperation1.execute();

        CreateOperationCommand createOperation2 = new CreateOperationCommand(
                operationRepository,
                accountRepository,
                Operation.OperationType.EXPENSE, accountId, 5000,
                new Date(System.currentTimeMillis() - 5L * 24 * 60 * 60 * 1000),
                "Продукты на неделю", foodCategory.getId()
        );
        ICommand timedOperation2 = new CommandTimerDecorator(createOperation2);
        timedOperation2.execute();

        CreateOperationCommand createOperation3 = new CreateOperationCommand(
                operationRepository,
                accountRepository,
                Operation.OperationType.EXPENSE, accountId, 1500,
                new Date(System.currentTimeMillis() - 3L * 24 * 60 * 60 * 1000),
                "Такси до работы", transportCategory.getId()
        );
        ICommand timedOperation3 = new CommandTimerDecorator(createOperation3);
        timedOperation3.execute();

        System.out.println("\n3.1 Обновление и удаление операций:");
        List<Operation> operations = operationService.getAllOperations();
        if (!operations.isEmpty()) {
            Operation firstOp = operations.get(0);
            operationService.updateOperation(
                    firstOp.getId(),
                    firstOp.getType(),
                    firstOp.getBankAccountId(),
                    firstOp.getAmount() + 1000,
                    firstOp.getDate(),
                    firstOp.getDescription() + " (премия)",
                    firstOp.getCategoryId()
            );
        }

        operations = operationService.getAllOperations();
        if (operations.size() > 2) {
            Operation thirdOp = operations.get(2);
            operationService.deleteOperation(thirdOp.getId());
        }

        System.out.println("\n4. Аналитика:");
        Date startDate = new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000);
        Date endDate = new Date();

        String balanceAnalytics = analyticsService.getBalanceAnalytics(startDate, endDate);
        System.out.println(balanceAnalytics);

        String categoryAnalytics = analyticsService.getCategoryAnalytics(startDate, endDate);
        System.out.println(categoryAnalytics);

        System.out.println("\n5. Демонстрация отмены операции:");
        timedOperation2.undo();

        System.out.println("\n6. Проверка и пересчет баланса:");
        boolean isBalanceCorrect = accountService.verifyBalance(accountId, operationRepository);
        System.out.println("Баланс корректен: " + isBalanceCorrect);

        if (!isBalanceCorrect) {
            System.out.println("Выполняем пересчет баланса...");
            accountService.recalculateBalance(accountId, operationRepository);
        }

        System.out.println("\n7. Экспорт данных (Visitor):");
        try {
            exportService.exportToCsv("financial_data.csv");
            exportService.exportToJson("financial_data.json");
            exportService.exportToYaml("financial_data.yaml");
            System.out.println("Все форматы экспортированы успешно");
        } catch (IOException e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        }

        System.out.println("\n8. Импорт данных из JSON (Template Method):");
        try {
            exportService.exportToJson("import_demo.json");

            IBankAccountRepository importAccountRepo =
                    new BankAccountRepositoryProxy(new BankAccountRepository());
            ICategoryRepository importCategoryRepo =
                    new CategoryRepositoryProxy(new CategoryRepository());
            IOperationRepository importOperationRepo =
                    new OperationRepositoryProxy(new OperationRepository());

            JsonDataImporter jsonImporter =
                    new JsonDataImporter(importAccountRepo, importCategoryRepo, importOperationRepo);

            System.out.println("Начало импорта из файла: import_demo.json");
            jsonImporter.importData("import_demo.json");

            System.out.println("Импортированные счета:");
            importAccountRepo.getAll().forEach(System.out::println);

            System.out.println("Импортированные категории:");
            importCategoryRepo.getAll().forEach(System.out::println);

            System.out.println("Импортированные операции:");
            importOperationRepo.getAll().forEach(System.out::println);

            System.out.println("Импорт завершен успешно!");
        } catch (IOException e) {
            System.err.println("Ошибка импорта: " + e.getMessage());
        }

        System.out.println("\n9. Итоговое состояние счетов:");
        accountService.getAllAccounts().forEach(System.out::println);

        System.out.println("\n10. Все операции:");
        operationService.getAllOperations().forEach(System.out::println);

        System.out.println("\n11. Все категории:");
        categoryService.getAllCategories().forEach(System.out::println);

        cleanupTempFiles();
    }

    private static void cleanupTempFiles() {
        String[] filesToDelete = {
                "financial_data.csv",
                "financial_data.json",
                "financial_data.yaml",
                "import_demo.json"
        };
        for (String filename : filesToDelete) {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
