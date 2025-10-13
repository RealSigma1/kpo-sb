package com.zoo.entities;

public abstract class Herbivore extends Animal {
    protected int kindnessLevel;

    protected Herbivore(String name, int food, int number, int kindnessLevel) {
        super(name, food, number);
        setKindnessLevel(kindnessLevel);
    }

    public int getKindnessLevel() {
        return kindnessLevel;
    }

    public final void setKindnessLevel(int kindnessLevel) {
        if (kindnessLevel < 1 || kindnessLevel > 10) {
            throw new IllegalArgumentException("Kindness level must be between 1 and 10");
        }
        this.kindnessLevel = kindnessLevel;
    }

    public boolean canBeInContactZoo() {
        return kindnessLevel > 5 && isHealthy;
    }
}