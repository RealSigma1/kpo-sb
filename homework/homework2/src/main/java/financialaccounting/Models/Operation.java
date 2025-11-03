package financialaccounting.Models;

import java.util.Date;

public class Operation {
    public enum OperationType { INCOME, EXPENSE }

    private int id;
    private OperationType type;
    private int bankAccountId;
    private double amount;
    private Date date;
    private String description;
    private int categoryId;

    public Operation(int id, OperationType type, int bankAccountId, double amount,
                     Date date, String description, int categoryId) {
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public OperationType getType() { return type; }
    public void setType(OperationType type) { this.type = type; }
    public int getBankAccountId() { return bankAccountId; }
    public void setBankAccountId(int bankAccountId) { this.bankAccountId = bankAccountId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    @Override
    public String toString() {
        return String.format("%s: %.2f от %s - %s", type, amount, date, description);
    }
}