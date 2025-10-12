package zoo;

import org.zoo.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zoo.services.VeterinaryClinic;
import org.zoo.services.ZooService;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TestZooService {

    private ZooService zooService;

    @BeforeEach
    void setUp() {
        VeterinaryClinic alwaysHealthyClinic = new VeterinaryClinic() {
            @Override
            public boolean checkHealth(Animal animal) {
                animal.setHealthStatus(true);
                return true;
            }
        };

        zooService = new ZooService(alwaysHealthyClinic);
    }

    @Test
    void testAddAnimal_Successful() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 8);

        boolean result = zooService.addAnimal(monkey);

        assertTrue(result);
    }

    @Test
    void testAddThing() {
        Table table = new Table("TestTable", 2001);

        zooService.addThing(table);

        List<?> inventory = zooService.getAllInventory();
        assertTrue(inventory.size() > 0);
    }

    @Test
    void testCalculateTotalFood() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 8);
        Tiger tiger = new Tiger("TestTiger", 1002);

        zooService.addAnimal(monkey);
        zooService.addAnimal(tiger);

        int totalFood = zooService.calculateTotalFood();
        assertEquals(11, totalFood);
    }

    @Test
    void testGetAnimalsForContactZoo() {
        Monkey kindMonkey = new Monkey("KindMonkey", 1001, 7);
        Rabbit unkindRabbit = new Rabbit("UnkindRabbit", 1002, 3);
        Tiger tiger = new Tiger("Tiger", 1003);

        zooService.addAnimal(kindMonkey);
        zooService.addAnimal(unkindRabbit);
        zooService.addAnimal(tiger);

        List<Herbivore> contactZooAnimals = zooService.getAnimalsForContactZoo();

        assertEquals(1, contactZooAnimals.size());
        assertEquals("KindMonkey", contactZooAnimals.get(0).getName());
    }

    @Test
    void testGetAllInventory() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 6);
        Table table = new Table("TestTable", 2001);

        zooService.addAnimal(monkey);
        zooService.addThing(table);

        List<?> inventory = zooService.getAllInventory();

        assertTrue(inventory.size() >= 2);
    }
}