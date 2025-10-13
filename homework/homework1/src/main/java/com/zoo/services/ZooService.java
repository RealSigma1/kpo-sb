package com.zoo.services;

import com.zoo.entities.*;

import java.util.ArrayList;
import java.util.List;

public class ZooService {
    private List<Animal> animals = new ArrayList<>();
    private List<Thing> things = new ArrayList<>();
    private VeterinaryClinic veterinaryClinic;

    public ZooService(VeterinaryClinic veterinaryClinic) {
        this.veterinaryClinic = veterinaryClinic;
    }

    public boolean addAnimal(Animal animal) {
        if (veterinaryClinic.checkHealth(animal)) {
            animals.add(animal);
            return true;
        }
        return false;
    }

    public void addThing(Thing thing) {
        things.add(thing);
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<Thing> getThings() {
        return things;
    }

    public int calculateTotalFood() {
        int totalFood = 0;
        for (Animal animal : animals) {
            totalFood += animal.getFood();
        }
        return totalFood;
    }

    public List<Herbivore> getAnimalsForContactZoo() {
        List<Herbivore> contactZooAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal instanceof Herbivore) {
                Herbivore herbivore = (Herbivore) animal;
                if (herbivore.canBeInContactZoo()) {
                    contactZooAnimals.add(herbivore);
                }
            }
        }
        return contactZooAnimals;
    }

    public boolean updateAnimalFood(Animal animal, int newFoodAmount) {
        if (newFoodAmount <= 0) {
            System.out.println("Food amount must be positive!");
            return false;
        }

        try {
            animal.setFood(newFoodAmount);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public void printReport() {
        System.out.println("=== ZOO MANAGEMENT REPORT ===");

        printAnimalsSection();

        printThingsSection();

        printContactZooSection();

        printFoodSummary();
    }

    private void printAnimalsSection() {
        System.out.println("\n--- ANIMALS IN ZOO ---");
        System.out.println("Total animals: " + animals.size());

        if (animals.isEmpty()) {
            System.out.println("No animals in the zoo.");
        } else {
            System.out.println("\nAnimal Details:");
            for (Animal animal : animals) {
                String type = getAnimalType(animal);
                String healthStatus = animal.isHealthy() ? "Healthy" : "Not healthy";

                System.out.printf("  %s: %s\n", type, animal.getName());
                System.out.printf("    Food: %d kg/day | Number #: %d | Status: %s",
                        animal.getFood(), animal.getNumber(), healthStatus);

                if (animal instanceof Herbivore) {
                    Herbivore herbivore = (Herbivore) animal;
                    System.out.printf(" | Kindness: %d/10", herbivore.getKindnessLevel());
                }
                System.out.println();
            }
        }
    }

    private void printThingsSection() {
        System.out.println("\n--- THING INVENTORY ---");
        System.out.println("Total items: " + things.size());

        if (things.isEmpty()) {
            System.out.println("No inventory items.");
        } else {
            System.out.println("\nInventory Details:");
            for (Thing thing : things) {
                String type = getThingType(thing);
                System.out.printf("  %s: %s (Inventory #: %d)\n",
                        type, thing.getName(), thing.getNumber());
            }
        }
    }

    private void printContactZooSection() {
        System.out.println("\n--- CONTACT ZOO ---");
        List<Herbivore> contactZooAnimals = getAnimalsForContactZoo();

        if (contactZooAnimals.isEmpty()) {
            System.out.println("No animals available for contact zoo.");
        } else {
            System.out.println("Animals suitable for contact zoo:");
            for (Herbivore animal : contactZooAnimals) {
                System.out.printf("  %s (Kindness: %d/10, Food: %d kg/day)\n",
                        animal.getName(), animal.getKindnessLevel(), animal.getFood());
            }
        }
    }

    private void printFoodSummary() {
        System.out.println("\n--- FOOD SUMMARY ---");
        System.out.println("Total food required: " + calculateTotalFood() + " kg/day");

        if (!animals.isEmpty()) {
            System.out.println("Daily food distribution:");
            for (Animal animal : animals) {
                System.out.printf("  %s: %d kg\n", animal.getName(), animal.getFood());
            }
        }
    }

    private String getAnimalType(Animal animal) {
        if (animal instanceof Monkey) return "Monkey";
        if (animal instanceof Rabbit) return "Rabbit";
        if (animal instanceof Tiger) return "Tiger";
        if (animal instanceof Wolf) return "Wolf";
        return "Unknown Animal";
    }

    private String getThingType(Thing thing) {
        if (thing instanceof Table) return "Table";
        if (thing instanceof Computer) return "Computer";
        return "Unknown Item";
    }

}