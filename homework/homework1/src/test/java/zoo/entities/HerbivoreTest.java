package zoo.entities;

import com.zoo.entities.Herbivore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HerbivoreTest {

    @Test
    void testHerbivoreCreation_ValidKindness() {
        TestHerbivore herbivore = new TestHerbivore("Test", 5, 1001, 7);
        assertEquals(7, herbivore.getKindnessLevel());
    }

    @Test
    void testHerbivoreCreation_InvalidKindness() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TestHerbivore("Test", 5, 1001, 15); // Доброта > 10
        });

        assertTrue(exception.getMessage().contains("Kindness level must be between 1 and 10"));
    }

    @Test
    void testHerbivoreCreation_ZeroKindness() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TestHerbivore("Test", 5, 1001, 0); // Доброта = 0
        });

        assertTrue(exception.getMessage().contains("Kindness level must be between 1 and 10"));
    }

    @Test
    void testCanBeInContactZoo_HealthyAndKind() {
        TestHerbivore herbivore = new TestHerbivore("Test", 5, 1001, 7);
        herbivore.setHealthStatus(true);
        assertTrue(herbivore.canBeInContactZoo());
    }

    @Test
    void testCanBeInContactZoo_HealthyButNotKind() {
        TestHerbivore herbivore = new TestHerbivore("Test", 5, 1001, 4);
        herbivore.setHealthStatus(true);
        assertFalse(herbivore.canBeInContactZoo());
    }

    @Test
    void testCanBeInContactZoo_KindButNotHealthy() {
        TestHerbivore herbivore = new TestHerbivore("Test", 5, 1001, 7);
        herbivore.setHealthStatus(false);
        assertFalse(herbivore.canBeInContactZoo());
    }

    // Вспомогательный класс для тестирования
    private static class TestHerbivore extends Herbivore {
        TestHerbivore(String name, int food, int number, int kindnessLevel) {
            super(name, food, number, kindnessLevel);
        }

        @Override
        public String getInventoryInfo() {
            return "TestHerbivore: " + getName() + ", Number: " + getNumber();
        }
    }
}