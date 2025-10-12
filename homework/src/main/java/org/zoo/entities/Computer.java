package org.zoo.entities;

public class Computer extends Thing {
    public Computer(String name, int number) {
        super(name, number);
    }

    @Override
    public String getInventoryInfo() {
        return "Computer: " + name + ", Inventory #: " + number;
    }
}