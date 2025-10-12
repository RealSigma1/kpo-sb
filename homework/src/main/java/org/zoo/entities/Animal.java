package org.zoo.entities;

import org.zoo.interfaces.IInventory;

public abstract class Animal extends LivingEntity implements IInventory {
    protected int number;
    protected boolean isHealthy;

    protected Animal(String name, int food, int number) {
        super(name, food);
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public void setHealthStatus(boolean isHealthy) {
        this.isHealthy = isHealthy;
    }

    public abstract String getInventoryInfo();
}