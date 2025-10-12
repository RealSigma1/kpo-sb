package org.zoo.entities;

import org.zoo.interfaces.IAlive;

public abstract class LivingEntity implements IAlive {
    protected String name;
    protected int food;

    protected LivingEntity(String name, int food) {
        this.name = name;
        this.food = food;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        if (food > 0) {
            this.food = food;
        } else {
            throw new IllegalArgumentException("Food amount must be positive");
        }
    }
}