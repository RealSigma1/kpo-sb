package financialaccounting.Patterns.Visitor;

import financialaccounting.Models.BankAccount;
import financialaccounting.Models.Category;
import financialaccounting.Models.Operation;

public interface IExportVisitor {
    void visit(BankAccount account);
    void visit(Category category);
    void visit(Operation operation);
    String getResult();
    String getFileExtension();
}