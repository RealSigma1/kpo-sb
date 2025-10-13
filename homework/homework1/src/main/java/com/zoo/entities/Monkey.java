package com.zoo.entities;

public class Monkey extends Herbivore {
    public Monkey(String name, int number, int kindnessLevel) {
        super(name, 3, number, kindnessLevel);
    }

    @Override
    public String getInventoryInfo() {
        return "Monkey: " + name + ", Inventory #: " + number;
    }
}