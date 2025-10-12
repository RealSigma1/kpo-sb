package org.zoo.entities;

import java.util.Scanner;

public abstract class Herbivore extends Animal {
    protected int kindnessLevel;
    private static Scanner scanner = new Scanner(System.in);

    protected Herbivore(String name, int food, int number, int kindnessLevel) {
        super(name, food, number);

        this.kindnessLevel = validateAndGetKindness(kindnessLevel);
    }

    private int validateAndGetKindness(int initialKindness) {
        int kindness = initialKindness;

        while (true) {
            if (kindness >= 1 && kindness <= 10) {
                return kindness;
            } else {
                System.out.println("negative! Kindness level must be between 1 and 10. Please try again.");
                System.out.print("Enter kindness level (1-10): ");

                try {
                    kindness = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("negative! Please enter a valid number.");
                    kindness = -1;
                }
            }
        }
    }

    public int getKindnessLevel() {
        return kindnessLevel;
    }

    public boolean canBeInContactZoo() {
        return kindnessLevel > 5 && isHealthy;
    }
}