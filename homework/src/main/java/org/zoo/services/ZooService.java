package org.zoo.services;

import org.zoo.entities.*;
import org.zoo.interfaces.IInventory;

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

    public List<IInventory> getAllInventory() {
        List<IInventory> inventory = new ArrayList<>();
        inventory.addAll(animals);
        inventory.addAll(things);
        return inventory;
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
        System.out.println("=== ZOO REPORT ===");
        System.out.println("Total animals: " + animals.size());
        System.out.println("Total food required: " + calculateTotalFood() + " kg/day");

        System.out.println("\nDetailed animal information:");
        if (animals.isEmpty()) {
            System.out.println("No animals in the zoo.");
        } else {
            for (Animal animal : animals) {
                String type = getAnimalType(animal);
                String healthStatus = animal.isHealthy() ? "Healthy" : "Not healthy";
                System.out.printf("- %s: %s (Food: %d kg/day, Inventory #: %d, Status: %s)",
                        type, animal.getName(), animal.getFood(), animal.getNumber(), healthStatus);

                if (animal instanceof Herbivore) {
                    Herbivore herbivore = (Herbivore) animal;
                    System.out.printf(" [Kindness: %d/10]", herbivore.getKindnessLevel());
                }
                System.out.println();
            }
        }

        System.out.println("\nAnimals for contact zoo:");
        List<Herbivore> contactZooAnimals = getAnimalsForContactZoo();
        if (contactZooAnimals.isEmpty()) {
            System.out.println("No animals available for contact zoo.");
        } else {
            for (Herbivore animal : contactZooAnimals) {
                System.out.printf("- %s (Kindness: %d/10, Food: %d kg/day)%n",
                        animal.getName(), animal.getKindnessLevel(), animal.getFood());
            }
        }

        System.out.println("\nInventory items:");
        List<IInventory> inventory = getAllInventory();
        if (inventory.isEmpty()) {
            System.out.println("No inventory items.");
        } else {
            for (IInventory item : inventory) {
                System.out.println("- " + item.getInventoryInfo());
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
}