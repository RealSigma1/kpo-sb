import com.zoo.entities.*;
import com.zoo.services.VeterinaryClinic;
import com.zoo.services.ZooService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        List<Thing> things = zooService.getThings();
        assertFalse(things.isEmpty());
        assertEquals("TestTable", things.get(0).getName());
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

        zooService.addAnimal(kindMonkey);
        zooService.addAnimal(unkindRabbit);

        List<Herbivore> contactZooAnimals = zooService.getAnimalsForContactZoo();

        assertEquals(1, contactZooAnimals.size());
        assertEquals("KindMonkey", contactZooAnimals.get(0).getName());
    }

    @Test
    void testGetAnimals() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 6);
        zooService.addAnimal(monkey);

        List<Animal> animals = zooService.getAnimals();
        assertEquals(1, animals.size());
        assertEquals("TestMonkey", animals.get(0).getName());
    }

    @Test
    void testGetThings() {
        Table table = new Table("TestTable", 2001);
        Computer computer = new Computer("TestComputer", 2002);

        zooService.addThing(table);
        zooService.addThing(computer);

        List<Thing> things = zooService.getThings();
        assertEquals(2, things.size());
    }

    @Test
    void testUpdateAnimalFood() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 6);
        zooService.addAnimal(monkey);

        boolean result = zooService.updateAnimalFood(monkey, 5);
        assertTrue(result);
        assertEquals(5, monkey.getFood());
    }

    @Test
    void testUpdateAnimalFood_NegativeAmount() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 6);
        zooService.addAnimal(monkey);

        boolean result = zooService.updateAnimalFood(monkey, -5);
        assertFalse(result);
    }

    @Test
    void testSeparateAnimalsAndThings() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 6);
        Table table = new Table("TestTable", 2001);
        Computer computer = new Computer("TestComputer", 2002);

        zooService.addAnimal(monkey);
        zooService.addThing(table);
        zooService.addThing(computer);

        List<Animal> animals = zooService.getAnimals();
        List<Thing> things = zooService.getThings();

        assertEquals(1, animals.size());
        assertEquals(2, things.size());
        assertNotEquals(animals.size(), things.size());
    }

    @Test
    void testEmptyCollections() {
        List<Animal> animals = zooService.getAnimals();
        List<Thing> things = zooService.getThings();

        assertTrue(animals.isEmpty());
        assertTrue(things.isEmpty());
    }
}