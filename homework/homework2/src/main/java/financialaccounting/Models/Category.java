package financialaccounting.Models;

public class Category {
    public enum CategoryType { INCOME, EXPENSE }

    private int id;
    private CategoryType type;
    private String name;

    public Category(int id, CategoryType type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public CategoryType getType() { return type; }
    public void setType(CategoryType type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return String.format("%s (ID: %d, Тип: %s)", name, id, type);
    }
}