package org.zoo.entities;

public class Rabbit extends Herbivore {
    public Rabbit(String name, int number, int kindnessLevel) {
        super(name, 1, number, kindnessLevel);
    }

    @Override
    public String getInventoryInfo() {
        return "Rabbit: " + name + ", Inventory #: " + number;
    }
}