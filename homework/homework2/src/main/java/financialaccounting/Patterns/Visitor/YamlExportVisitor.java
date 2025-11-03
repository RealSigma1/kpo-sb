package financialaccounting.Patterns.Visitor;

import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Category;
import financialaccounting.Models.Operation;
import java.text.SimpleDateFormat;

public class YamlExportVisitor implements IExportVisitor {
    private final StringBuilder stringBuilder = new StringBuilder();

    public YamlExportVisitor() {
        stringBuilder.append("entities:\n");
    }

    @Override
    public void visit(BankAccount account) {
        stringBuilder.append(String.format(
                "- type: Account\n  id: %d\n  name: \"%s\"\n  balance: %.2f\n",
                account.getId(), account.getName(), account.getBalance()));
    }

    @Override
    public void visit(Category category) {
        stringBuilder.append(String.format(
                "- type: Category\n  id: %d\n  categoryType: \"%s\"\n  name: \"%s\"\n",
                category.getId(), category.getType(), category.getName()));
    }

    @Override
    public void visit(Operation operation) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        stringBuilder.append(String.format(
                "- type: Operation\n  id: %d\n  operationType: \"%s\"\n  bankAccountId: %d\n  amount: %.2f\n  date: \"%s\"\n  description: \"%s\"\n  categoryId: %d\n",
                operation.getId(), operation.getType(), operation.getBankAccountId(),
                operation.getAmount(), dateFormat.format(operation.getDate()),
                operation.getDescription(), operation.getCategoryId()));
    }

    @Override
    public String getResult() {
        return stringBuilder.toString();
    }

    @Override
    public String getFileExtension() {
        return "yaml";
    }
}