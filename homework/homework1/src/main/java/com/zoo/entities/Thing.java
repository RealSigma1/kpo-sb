package com.zoo.entities;

import com.zoo.interfaces.IInventory;

public abstract class Thing implements IInventory {
    protected String name;
    protected int number;

    protected Thing(String name, int number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumber() {
        return number;
    }

    public abstract String getInventoryInfo();
}
