package org.zoo.entities;

public class Table extends Thing {
    public Table(String name, int number) {
        super(name, number);
    }

    @Override
    public String getInventoryInfo() {
        return "Table: " + name + ", Inventory #: " + number;
    }
}