package org.zoo.entities;

public class Tiger extends Predator {
    public Tiger(String name, int number) {
        super(name, 8, number);
    }

    @Override
    public String getInventoryInfo() {
        return "Tiger: " + name + ", Inventory #: " + number;
    }
}