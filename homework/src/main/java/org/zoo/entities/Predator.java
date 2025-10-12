package org.zoo.entities;

public abstract class Predator extends Animal {
    protected Predator(String name, int food, int number) {
        super(name, food, number);
    }

    public boolean canBeInContactZoo() {
        return false;
    }
}