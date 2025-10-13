package zoo.services;

import com.zoo.entities.*;
import com.zoo.services.VeterinaryClinic;
import com.zoo.services.ZooService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ZooServiceTest {

    private ZooService zooService;
    private VeterinaryClinic clinic;

    @BeforeEach
    void setUp() {
        clinic = new VeterinaryClinic();
        zooService = new ZooService(clinic);
    }

    @Test
    void testAddAnimal_SuccessfulWithHealthyAnimal() {
        VeterinaryClinic healthyClinic = new VeterinaryClinic() {
            @Override
            public boolean checkHealth(Animal animal) {
                animal.setHealthStatus(true);
                return true;
            }
        };
        ZooService healthyZooService = new ZooService(healthyClinic);

        Monkey monkey = new Monkey("HealthyMonkey", 1001, 7);

        boolean result = healthyZooService.addAnimal(monkey);

        assertTrue(result);
        assertEquals(1, healthyZooService.getAnimals().size());
        assertEquals("HealthyMonkey", healthyZooService.getAnimals().get(0).getName());
    }

    @Test
    void testAddAnimal_RejectedDueToHealth() {
        VeterinaryClinic sickClinic = new VeterinaryClinic() {
            @Override
            public boolean checkHealth(Animal animal) {
                animal.setHealthStatus(false);
                return false;
            }
        };
        ZooService sickZooService = new ZooService(sickClinic);

        Monkey monkey = new Monkey("SickMonkey", 1001, 7);

        boolean result = sickZooService.addAnimal(monkey);

        assertFalse(result);
        assertEquals(0, sickZooService.getAnimals().size());
    }

    @Test
    void testAddAnimal_MultipleAnimals() {
        VeterinaryClinic healthyClinic = new VeterinaryClinic() {
            @Override
            public boolean checkHealth(Animal animal) {
                animal.setHealthStatus(true);
                return true;
            }
        };
        ZooService healthyZooService = new ZooService(healthyClinic);

        Monkey monkey = new Monkey("Monkey1", 1001, 7);
        Rabbit rabbit = new Rabbit("Rabbit1", 1002, 6);
        Tiger tiger = new Tiger("Tiger1", 1003);

        healthyZooService.addAnimal(monkey);
        healthyZooService.addAnimal(rabbit);
        healthyZooService.addAnimal(tiger);

        List<Animal> animals = healthyZooService.getAnimals();
        assertEquals(3, animals.size());
        assertTrue(animals.stream().anyMatch(a -> a.getName().equals("Monkey1")));
        assertTrue(animals.stream().anyMatch(a -> a.getName().equals("Rabbit1")));
        assertTrue(animals.stream().anyMatch(a -> a.getName().equals("Tiger1")));
    }

    @Test
    void testAddThing_SingleThing() {
        Table table = new Table("Office Table", 2001);

        zooService.addThing(table);

        List<Thing> things = zooService.getThings();
        assertEquals(1, things.size());
        assertEquals("Office Table", things.get(0).getName());
        assertEquals(2001, things.get(0).getNumber());
        assertTrue(things.get(0) instanceof Table);
    }

    @Test
    void testAddThing_MultipleThings() {
        Table table = new Table("Office Table", 2001);
        Computer computer = new Computer("Admin PC", 2002);
        Table table2 = new Table("Reception Table", 2003);

        zooService.addThing(table);
        zooService.addThing(computer);
        zooService.addThing(table2);

        List<Thing> things = zooService.getThings();
        assertEquals(3, things.size());
    }

    @Test
    void testCalculateTotalFood_EmptyZoo() {
        int totalFood = zooService.calculateTotalFood();
        assertEquals(0, totalFood);
    }

    @Test
    void testCalculateTotalFood_SingleAnimal() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 8);
        monkey.setHealthStatus(true);
        zooService.getAnimals().add(monkey);

        int totalFood = zooService.calculateTotalFood();
        assertEquals(3, totalFood); // Обезьяна ест 3 кг
    }

    @Test
    void testCalculateTotalFood_MultipleAnimals() {
        Monkey monkey = new Monkey("Monkey", 1001, 8);
        Rabbit rabbit = new Rabbit("Rabbit", 1002, 6);
        Tiger tiger = new Tiger("Tiger", 1003);
        Wolf wolf = new Wolf("Wolf", 1004);

        monkey.setHealthStatus(true);
        rabbit.setHealthStatus(true);
        tiger.setHealthStatus(true);
        wolf.setHealthStatus(true);

        zooService.getAnimals().add(monkey);
        zooService.getAnimals().add(rabbit);
        zooService.getAnimals().add(tiger);
        zooService.getAnimals().add(wolf);

        int totalFood = zooService.calculateTotalFood();
        assertEquals(18, totalFood);
    }

    @Test
    void testGetAnimalsForContactZoo_Empty() {
        List<Herbivore> contactZooAnimals = zooService.getAnimalsForContactZoo();
        assertTrue(contactZooAnimals.isEmpty());
    }

    @Test
    void testGetAnimalsForContactZoo_OnlyHerbivores() {
        Monkey kindMonkey = new Monkey("KindMonkey", 1001, 7);
        Rabbit unkindRabbit = new Rabbit("UnkindRabbit", 1002, 3);
        Tiger tiger = new Tiger("Tiger", 1003);

        kindMonkey.setHealthStatus(true);
        unkindRabbit.setHealthStatus(true);
        tiger.setHealthStatus(true);

        zooService.getAnimals().add(kindMonkey);
        zooService.getAnimals().add(unkindRabbit);
        zooService.getAnimals().add(tiger);

        List<Herbivore> contactZooAnimals = zooService.getAnimalsForContactZoo();

        assertEquals(1, contactZooAnimals.size());
        assertEquals("KindMonkey", contactZooAnimals.get(0).getName());
    }

    @Test
    void testGetAnimalsForContactZoo_HealthyAndKind() {
        Monkey monkey1 = new Monkey("VeryKind", 1001, 9);
        Monkey monkey2 = new Monkey("Kind", 1002, 7);
        Rabbit rabbit1 = new Rabbit("VeryKindRabbit", 1003, 8);
        Rabbit rabbit2 = new Rabbit("NotKind", 1004, 4);

        monkey1.setHealthStatus(true);
        monkey2.setHealthStatus(true);
        rabbit1.setHealthStatus(true);
        rabbit2.setHealthStatus(true);

        zooService.getAnimals().add(monkey1);
        zooService.getAnimals().add(monkey2);
        zooService.getAnimals().add(rabbit1);
        zooService.getAnimals().add(rabbit2);

        List<Herbivore> contactZooAnimals = zooService.getAnimalsForContactZoo();

        assertEquals(3, contactZooAnimals.size());
        assertTrue(contactZooAnimals.stream().anyMatch(a -> a.getName().equals("VeryKind")));
        assertTrue(contactZooAnimals.stream().anyMatch(a -> a.getName().equals("Kind")));
        assertTrue(contactZooAnimals.stream().anyMatch(a -> a.getName().equals("VeryKindRabbit")));
    }

    @Test
    void testGetAnimalsForContactZoo_UnhealthyAnimals() {
        Monkey kindMonkey = new Monkey("KindButSick", 1001, 8);
        Rabbit kindRabbit = new Rabbit("KindAndHealthy", 1002, 7);

        kindMonkey.setHealthStatus(false);
        kindRabbit.setHealthStatus(true);

        zooService.getAnimals().add(kindMonkey);
        zooService.getAnimals().add(kindRabbit);

        List<Herbivore> contactZooAnimals = zooService.getAnimalsForContactZoo();

        assertEquals(1, contactZooAnimals.size());
        assertEquals("KindAndHealthy", contactZooAnimals.get(0).getName());
    }

    @Test
    void testUpdateAnimalFood_Successful() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 6);
        monkey.setHealthStatus(true);
        zooService.getAnimals().add(monkey);

        boolean result = zooService.updateAnimalFood(monkey, 5);

        assertTrue(result);
        assertEquals(5, monkey.getFood());
    }

    @Test
    void testUpdateAnimalFood_NegativeAmount() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 6);
        monkey.setHealthStatus(true);
        zooService.getAnimals().add(monkey);

        boolean result = zooService.updateAnimalFood(monkey, -5);

        assertFalse(result);
        assertEquals(3, monkey.getFood());
    }

    @Test
    void testUpdateAnimalFood_ZeroAmount() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 6);
        monkey.setHealthStatus(true);
        zooService.getAnimals().add(monkey);

        boolean result = zooService.updateAnimalFood(monkey, 0);

        assertFalse(result);
        assertEquals(3, monkey.getFood());
    }

    @Test
    void testUpdateAnimalFood_LargeAmount() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 6);
        monkey.setHealthStatus(true);
        zooService.getAnimals().add(monkey);

        boolean result = zooService.updateAnimalFood(monkey, 1000);

        assertTrue(result);
        assertEquals(1000, monkey.getFood());
    }

    @Test
    void testUpdateAnimalFood_AnimalNotInZoo() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 6);

        boolean result = zooService.updateAnimalFood(monkey, 5);

        assertDoesNotThrow(() -> zooService.updateAnimalFood(monkey, 5));
    }

    @Test
    void testGetAnimals_Empty() {
        List<Animal> animals = zooService.getAnimals();
        assertTrue(animals.isEmpty());
    }

    @Test
    void testGetAnimals_WithAnimals() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 6);
        monkey.setHealthStatus(true);
        zooService.getAnimals().add(monkey);

        List<Animal> animals = zooService.getAnimals();
        assertEquals(1, animals.size());
        assertEquals("TestMonkey", animals.get(0).getName());
    }

    @Test
    void testGetThings_Empty() {
        List<Thing> things = zooService.getThings();
        assertTrue(things.isEmpty());
    }

    @Test
    void testGetThings_WithThings() {
        Table table = new Table("TestTable", 2001);
        zooService.addThing(table);

        List<Thing> things = zooService.getThings();
        assertEquals(1, things.size());
        assertEquals("TestTable", things.get(0).getName());
    }

    @Test
    void testPrintReport_EmptyZoo() {
        assertDoesNotThrow(() -> zooService.printReport());
    }

    @Test
    void testPrintReport_WithAnimalsOnly() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 7);
        Tiger tiger = new Tiger("TestTiger", 1002);

        monkey.setHealthStatus(true);
        tiger.setHealthStatus(true);

        zooService.getAnimals().add(monkey);
        zooService.getAnimals().add(tiger);

        assertDoesNotThrow(() -> zooService.printReport());
    }

    @Test
    void testPrintReport_WithThingsOnly() {
        Table table = new Table("TestTable", 2001);
        Computer computer = new Computer("TestComputer", 2002);

        zooService.addThing(table);
        zooService.addThing(computer);

        assertDoesNotThrow(() -> zooService.printReport());
    }

    @Test
    void testPrintReport_WithMixedData() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 7);
        Rabbit rabbit = new Rabbit("TestRabbit", 1002, 4);
        Table table = new Table("TestTable", 2001);
        Computer computer = new Computer("TestComputer", 2002);

        monkey.setHealthStatus(true);
        rabbit.setHealthStatus(true);

        zooService.getAnimals().add(monkey);
        zooService.getAnimals().add(rabbit);
        zooService.addThing(table);
        zooService.addThing(computer);

        assertDoesNotThrow(() -> zooService.printReport());
    }

    @Test
    void testMixedAnimalsAndThings() {
        Monkey monkey = new Monkey("Monkey", 1001, 7);
        Tiger tiger = new Tiger("Tiger", 1002);
        Table table = new Table("Table", 2001);
        Computer computer = new Computer("Computer", 2002);

        monkey.setHealthStatus(true);
        tiger.setHealthStatus(true);

        zooService.getAnimals().add(monkey);
        zooService.getAnimals().add(tiger);
        zooService.addThing(table);
        zooService.addThing(computer);

        List<Animal> animals = zooService.getAnimals();
        List<Thing> things = zooService.getThings();

        assertEquals(2, animals.size());
        assertEquals(2, things.size());
    }

    @Test
    void testFoodCalculationAfterMultipleOperations() {
        VeterinaryClinic healthyClinic = new VeterinaryClinic() {
            @Override
            public boolean checkHealth(Animal animal) {
                animal.setHealthStatus(true);
                return true;
            }
        };
        ZooService healthyZooService = new ZooService(healthyClinic);

        Monkey monkey = new Monkey("Monkey", 1001, 7);
        Rabbit rabbit = new Rabbit("Rabbit", 1002, 6);

        healthyZooService.addAnimal(monkey);
        healthyZooService.addAnimal(rabbit);

        int initialFood = healthyZooService.calculateTotalFood();
        assertEquals(4, initialFood);

        healthyZooService.updateAnimalFood(monkey, 10);

        int updatedFood = healthyZooService.calculateTotalFood();
        assertEquals(11, updatedFood);
    }

    @Test
    void testContactZooAfterHealthChanges() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 8);
        monkey.setHealthStatus(true);
        zooService.getAnimals().add(monkey);

        List<Herbivore> initialContactZoo = zooService.getAnimalsForContactZoo();
        assertEquals(1, initialContactZoo.size());

        monkey.setHealthStatus(false);

        List<Herbivore> updatedContactZoo = zooService.getAnimalsForContactZoo();
        assertTrue(updatedContactZoo.isEmpty());
    }

    @Test
    void testAddAnimalWithRealHealthCheck() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 7);

        boolean result = zooService.addAnimal(monkey);

        assertEquals(result, monkey.isHealthy());
        if (result) {
            assertEquals(1, zooService.getAnimals().size());
        } else {
            assertEquals(0, zooService.getAnimals().size());
        }
    }
}