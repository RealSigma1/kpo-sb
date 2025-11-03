package financialaccounting.Patterns.TemplateMethod;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Interfaces.ICategoryRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Category;
import financialaccounting.Models.Operation;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvDataImporter extends DataImporter {

    public CsvDataImporter(IBankAccountRepository accountRepository,
                           ICategoryRepository categoryRepository,
                           IOperationRepository operationRepository) {
        super(accountRepository, categoryRepository, operationRepository);
    }

    @Override
    protected List<Object> parseData(String data) {
        List<Object> entities = new ArrayList<>();
        String[] lines = data.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] fields = line.split(",");
            if (fields.length < 2) continue;

            try {
                String type = fields[0];
                switch (type) {
                    case "Account":
                        if (fields.length >= 4) {
                            entities.add(parseBankAccount(fields));
                        }
                        break;
                    case "Category":
                        if (fields.length >= 6) {
                            entities.add(parseCategory(fields));
                        }
                        break;
                    case "Operation":
                        if (fields.length >= 12) {
                            entities.add(parseOperation(fields));
                        }
                        break;
                }
            } catch (Exception e) {
                System.err.println("Ошибка парсинга строки: " + line);
                e.printStackTrace();
            }
        }

        return entities;
    }
}