package financialaccounting.Patterns.Visitor;

import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Category;
import financialaccounting.Models.Operation;
import java.text.SimpleDateFormat;

public class JsonExportVisitor implements IExportVisitor {
    private final StringBuilder stringBuilder = new StringBuilder();
    private boolean firstElement = true;

    public JsonExportVisitor() {
        stringBuilder.append("[\n");
    }

    @Override
    public void visit(BankAccount account) {
        if (!firstElement) stringBuilder.append(",\n");
        stringBuilder.append(String.format(
                "  {\n    \"type\": \"Account\",\n    \"id\": %d,\n    \"name\": \"%s\",\n    \"balance\": %.2f\n  }",
                account.getId(), account.getName(), account.getBalance()));
        firstElement = false;
    }

    @Override
    public void visit(Category category) {
        if (!firstElement) stringBuilder.append(",\n");
        stringBuilder.append(String.format(
                "  {\n    \"type\": \"Category\",\n    \"id\": %d,\n    \"categoryType\": \"%s\",\n    \"name\": \"%s\"\n  }",
                category.getId(), category.getType(), category.getName()));
        firstElement = false;
    }

    @Override
    public void visit(Operation operation) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (!firstElement) stringBuilder.append(",\n");
        stringBuilder.append(String.format(
                "  {\n    \"type\": \"Operation\",\n    \"id\": %d,\n    \"operationType\": \"%s\",\n    \"bankAccountId\": %d,\n    \"amount\": %.2f,\n    \"date\": \"%s\",\n    \"description\": \"%s\",\n    \"categoryId\": %d\n  }",
                operation.getId(), operation.getType(), operation.getBankAccountId(),
                operation.getAmount(), dateFormat.format(operation.getDate()),
                operation.getDescription(), operation.getCategoryId()));
        firstElement = false;
    }

    @Override
    public String getResult() {
        stringBuilder.append("\n]");
        return stringBuilder.toString();
    }

    @Override
    public String getFileExtension() {
        return "json";
    }
}