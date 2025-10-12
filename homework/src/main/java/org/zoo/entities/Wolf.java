package org.zoo.entities;

public class Wolf extends Predator {
    public Wolf(String name, int number) {
        super(name, 6, number);
    }

    @Override
    public String getInventoryInfo() {
        return "Wolf: " + name + ", Inventory #: " + number;
    }
}