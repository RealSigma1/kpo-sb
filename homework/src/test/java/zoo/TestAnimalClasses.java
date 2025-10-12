package zoo;

import org.junit.jupiter.api.Test;
import org.zoo.entities.Monkey;
import org.zoo.entities.Rabbit;
import org.zoo.entities.Tiger;
import org.zoo.entities.Wolf;

import static org.junit.jupiter.api.Assertions.*;

class TestAnimalClasses {

    @Test
    void testMonkeyCreation() {
        Monkey monkey = new Monkey("Charlie", 1001, 7);

        assertEquals("Charlie", monkey.getName());
        assertEquals(3, monkey.getFood());
        assertEquals(1001, monkey.getNumber());
        assertEquals(7, monkey.getKindnessLevel());
        assertTrue(monkey.getInventoryInfo().contains("Monkey"));
    }

    @Test
    void testRabbitCreation() {
        Rabbit rabbit = new Rabbit("Bunny", 1002, 6);

        assertEquals("Bunny", rabbit.getName());
        assertEquals(1, rabbit.getFood());
        assertEquals(1002, rabbit.getNumber());
        assertEquals(6, rabbit.getKindnessLevel());
        assertTrue(rabbit.getInventoryInfo().contains("Rabbit"));
    }

    @Test
    void testTigerCreation() {
        Tiger tiger = new Tiger("Sher Khan", 1003);

        assertEquals("Sher Khan", tiger.getName());
        assertEquals(8, tiger.getFood());
        assertEquals(1003, tiger.getNumber());
        assertTrue(tiger.getInventoryInfo().contains("Tiger"));
    }

    @Test
    void testWolfCreation() {
        Wolf wolf = new Wolf("Grey", 1004);

        assertEquals("Grey", wolf.getName());
        assertEquals(6, wolf.getFood());
        assertEquals(1004, wolf.getNumber());
        assertTrue(wolf.getInventoryInfo().contains("Wolf"));
    }

    @Test
    void testHerbivoreCanBeInContactZoo() {
        Monkey kindMonkey = new Monkey("Kind", 1001, 7); // kindness > 5
        Monkey unkindMonkey = new Monkey("Unkind", 1002, 4); // kindness <= 5

        kindMonkey.setHealthStatus(true);
        unkindMonkey.setHealthStatus(true);

        assertTrue(kindMonkey.canBeInContactZoo());
        assertFalse(unkindMonkey.canBeInContactZoo());

        kindMonkey.setHealthStatus(false);
        assertFalse(kindMonkey.canBeInContactZoo());
    }

    @Test
    void testPredatorCannotBeInContactZoo() {
        Tiger tiger = new Tiger("Tiger", 1001);
        tiger.setHealthStatus(true);

        assertFalse(tiger.canBeInContactZoo());
    }
}