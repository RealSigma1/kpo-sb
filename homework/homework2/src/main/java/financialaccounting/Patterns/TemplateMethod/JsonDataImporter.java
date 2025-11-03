package financialaccounting.Patterns.TemplateMethod;

import financialaccounting.Interfaces.IBankAccountRepository;
import financialaccounting.Interfaces.ICategoryRepository;
import financialaccounting.Interfaces.IOperationRepository;
import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Category;
import financialaccounting.Models.Operation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonDataImporter extends DataImporter {

    public JsonDataImporter(IBankAccountRepository accountRepository,
                            ICategoryRepository categoryRepository,
                            IOperationRepository operationRepository) {
        super(accountRepository, categoryRepository, operationRepository);
    }

    @Override
    protected List<Object> parseData(String data) {
        List<Object> entities = new ArrayList<>();

        Pattern objectPattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = objectPattern.matcher(data);

        while (matcher.find()) {
            String objectContent = matcher.group(1);
            try {
                Object entity = parseJsonObject(objectContent);
                if (entity != null) {
                    entities.add(entity);
                }
            } catch (Exception e) {
                System.err.println("Ошибка парсинга JSON объекта: " + objectContent);
                e.printStackTrace();
            }
        }

        return entities;
    }

    private Object parseJsonObject(String jsonContent) throws ParseException {
        String type = extractField(jsonContent, "type");

        switch (type) {
            case "Account":
                return parseJsonAccount(jsonContent);
            case "Category":
                return parseJsonCategory(jsonContent);
            case "Operation":
                return parseJsonOperation(jsonContent);
            default:
                return null;
        }
    }

    private BankAccount parseJsonAccount(String jsonContent) {
        int id = Integer.parseInt(extractField(jsonContent, "id"));
        String name = extractField(jsonContent, "name");
        double balance = Double.parseDouble(extractField(jsonContent, "balance"));
        return new BankAccount(id, name, balance);
    }

    private Category parseJsonCategory(String jsonContent) {
        int id = Integer.parseInt(extractField(jsonContent, "id"));
        Category.CategoryType type = Category.CategoryType.valueOf(extractField(jsonContent, "categoryType"));
        String name = extractField(jsonContent, "name");
        return new Category(id, type, name);
    }

    private Operation parseJsonOperation(String jsonContent) throws ParseException {
        int id = Integer.parseInt(extractField(jsonContent, "id"));
        Operation.OperationType type = Operation.OperationType.valueOf(extractField(jsonContent, "operationType"));
        int accountId = Integer.parseInt(extractField(jsonContent, "bankAccountId"));
        double amount = Double.parseDouble(extractField(jsonContent, "amount"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = dateFormat.parse(extractField(jsonContent, "date"));

        String description = extractField(jsonContent, "description");
        int categoryId = Integer.parseInt(extractField(jsonContent, "categoryId"));

        return new Operation(id, type, accountId, amount, date, description, categoryId);
    }

    private String extractField(String jsonContent, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + fieldName + "\":\\s*\"?([^\",}]+)\"?");
        Matcher matcher = pattern.matcher(jsonContent);
        if (matcher.find()) {
            return matcher.group(1).replace("\"", "").trim();
        }
        return "";
    }
}