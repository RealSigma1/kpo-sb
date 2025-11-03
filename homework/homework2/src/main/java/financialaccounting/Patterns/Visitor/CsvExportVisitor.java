package financialaccounting.Patterns.Visitor;

import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Category;
import financialaccounting.Models.Operation;
import java.text.SimpleDateFormat;

public class CsvExportVisitor implements IExportVisitor {
    private final StringBuilder stringBuilder = new StringBuilder();

    public CsvExportVisitor() {
        stringBuilder.append("Type,ID,Name,Balance,CategoryType,AccountId,Amount,Date,Description,CategoryId\n");
    }

    @Override
    public void visit(BankAccount account) {
        stringBuilder.append(String.format("Account,%d,%s,%.2f,,,,,,\n",
                account.getId(), account.getName(), account.getBalance()));
    }

    @Override
    public void visit(Category category) {
        stringBuilder.append(String.format("Category,%d,,,%s,%s,,,,,\n",
                category.getId(), category.getType(), category.getName()));
    }

    @Override
    public void visit(Operation operation) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        stringBuilder.append(String.format("Operation,%d,,,,%d,%.2f,%s,%s,%d\n",
                operation.getId(), operation.getBankAccountId(), operation.getAmount(),
                dateFormat.format(operation.getDate()), operation.getDescription(), operation.getCategoryId()));
    }

    @Override
    public String getResult() {
        return stringBuilder.toString();
    }

    @Override
    public String getFileExtension() {
        return "csv";
    }
}