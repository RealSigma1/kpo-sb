package org.zoo;

import org.zoo.container.ServiceContainer;
import org.zoo.entities.*;
import org.zoo.services.VeterinaryClinic;
import org.zoo.services.ZooService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static ZooService zooService;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ServiceContainer container = new ServiceContainer();
        container.registerSingleton(VeterinaryClinic.class, new VeterinaryClinic());
        container.registerSingleton(ZooService.class,
                new ZooService(container.getService(VeterinaryClinic.class)));

        zooService = container.getService(ZooService.class);

        addSampleData();

        runMenu();
    }

    private static void addSampleData() {
        Monkey monkey = new Monkey("Gorilla", 1001, 7);
        Rabbit rabbit = new Rabbit("Bugs Bunny", 1002, 6);
        Tiger tiger = new Tiger("Me", 1003);
        Wolf wolf = new Wolf("Happy", 1004);

        zooService.addAnimal(monkey);
        zooService.addAnimal(rabbit);
        zooService.addAnimal(tiger);
        zooService.addAnimal(wolf);

        zooService.addThing(new Table("Office Table", 2001));
        zooService.addThing(new Computer("Admin PC", 2002));

        System.out.println("Sample data added successfully!");
    }

    private static void runMenu() {
        while (true) {
            System.out.println("\n=== MOSCOW ZOO MANAGEMENT SYSTEM ===");
            System.out.println("1. View Report");
            System.out.println("2. Add New Animal");
            System.out.println("3. Add New Thing");
            System.out.println("4. Update Animal Food");
            System.out.println("5. Exit");
            System.out.print("Select option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    zooService.printReport();
                    break;
                case "2":
                    addNewAnimal();
                    break;
                case "3":
                    addNewThing();
                    break;
                case "4":
                    updateAnimalFood();
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void addNewAnimal() {
        System.out.println("\n=== ADD NEW ANIMAL ===");
        System.out.println("1. Monkey");
        System.out.println("2. Rabbit");
        System.out.println("3. Tiger");
        System.out.println("4. Wolf");
        System.out.print("Select animal type: ");

        String typeChoice = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter inventory number: ");
        int number = Integer.parseInt(scanner.nextLine());

        Animal animal = null;

        switch (typeChoice) {
            case "1":
                System.out.print("Enter kindness level (1-10): ");
                int kindness = Integer.parseInt(scanner.nextLine());
                animal = new Monkey(name, number, kindness);
                break;
            case "2":
                System.out.print("Enter kindness level (1-10): ");
                kindness = Integer.parseInt(scanner.nextLine());
                animal = new Rabbit(name, number, kindness);
                break;
            case "3":
                animal = new Tiger(name, number);
                break;
            case "4":
                animal = new Wolf(name, number);
                break;
            default:
                System.out.println("Invalid animal type!");
                return;
        }

        if (zooService.addAnimal(animal)) {
            System.out.println("positive " + animal.getName() + " successfully added to the zoo!");
        } else {
            System.out.println("negative " + animal.getName() + " was not accepted due to health issues.");
        }
    }

    private static void addNewThing() {
        System.out.println("\n=== ADD NEW THING ===");
        System.out.println("1. Table");
        System.out.println("2. Computer");
        System.out.print("Select thing type: ");

        String typeChoice = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter inventory number: ");
        int number = Integer.parseInt(scanner.nextLine());

        Thing thing = null;

        switch (typeChoice) {
            case "1":
                thing = new Table(name, number);
                break;
            case "2":
                thing = new Computer(name, number);
                break;
            default:
                System.out.println("Invalid thing type!");
                return;
        }

        zooService.addThing(thing);
        System.out.println("✅ " + thing.getName() + " successfully added to inventory!");
    }

    private static void updateAnimalFood() {
        System.out.println("\n=== UPDATE ANIMAL FOOD ===");

        // Показываем текущих животных
        List<Animal> animals = zooService.getAnimals();
        if (animals.isEmpty()) {
            System.out.println("No animals in the zoo!");
            return;
        }

        System.out.println("Current animals:");
        for (int i = 0; i < animals.size(); i++) {
            Animal animal = animals.get(i);
            System.out.println((i + 1) + ". " + animal.getName() +
                    " - Current food: " + animal.getFood() + "kg");
        }

        System.out.print("Select animal number: ");
        int animalIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (animalIndex < 0 || animalIndex >= animals.size()) {
            System.out.println("Invalid animal number!");
            return;
        }

        System.out.print("Enter new food amount (kg): ");
        int newFood = Integer.parseInt(scanner.nextLine());

        Animal selectedAnimal = animals.get(animalIndex);

        if (zooService.updateAnimalFood(selectedAnimal, newFood)) {
            System.out.println("positive! " + selectedAnimal.getName() +
                    " food updated to " + newFood + "kg");
        } else {
            System.out.println("negative! Failed to update food amount");
        }
    }
}