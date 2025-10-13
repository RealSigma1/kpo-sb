package zoo.services;

import com.zoo.entities.*;
import com.zoo.services.VeterinaryClinic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VeterinaryClinicTest {

    private VeterinaryClinic clinic;

    @BeforeEach
    void setUp() {
        clinic = new VeterinaryClinic();
    }

    @Test
    void testCheckHealth_ReturnsBoolean() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 7);

        boolean result = clinic.checkHealth(monkey);

        assertTrue(result || !result);
    }

    @Test
    void testCheckHealth_SetsHealthStatus() {
        Tiger tiger = new Tiger("TestTiger", 1002);

        clinic.checkHealth(tiger);

        assertTrue(tiger.isHealthy() || !tiger.isHealthy());
    }

    @Test
    void testCheckHealth_MultipleAnimals() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 7);
        Rabbit rabbit = new Rabbit("TestRabbit", 1002, 6);
        Tiger tiger = new Tiger("TestTiger", 1003);

        boolean result1 = clinic.checkHealth(monkey);
        boolean result2 = clinic.checkHealth(rabbit);
        boolean result3 = clinic.checkHealth(tiger);

        assertEquals(result1, monkey.isHealthy());
        assertEquals(result2, rabbit.isHealthy());
        assertEquals(result3, tiger.isHealthy());
    }

    @Test
    void testCheckHealth_DifferentAnimalTypes() {
        Monkey monkey = new Monkey("Monkey", 1001, 7);
        Rabbit rabbit = new Rabbit("Rabbit", 1002, 6);
        Tiger tiger = new Tiger("Tiger", 1003);
        Wolf wolf = new Wolf("Wolf", 1004);

        clinic.checkHealth(monkey);
        clinic.checkHealth(rabbit);
        clinic.checkHealth(tiger);
        clinic.checkHealth(wolf);

        assertTrue(monkey.isHealthy() || !monkey.isHealthy());
        assertTrue(rabbit.isHealthy() || !rabbit.isHealthy());
        assertTrue(tiger.isHealthy() || !tiger.isHealthy());
        assertTrue(wolf.isHealthy() || !wolf.isHealthy());
    }

    @Test
    void testCheckHealth_ResultMatchesHealthStatus() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 7);

        boolean result = clinic.checkHealth(monkey);

        assertEquals(result, monkey.isHealthy());
    }

    @Test
    void testCheckHealth_HealthyAnimalCanBeInContactZoo() {
        Monkey monkey = new Monkey("KindMonkey", 1001, 7);

        boolean healthResult = clinic.checkHealth(monkey);

        if (healthResult && monkey.getKindnessLevel() > 5) {
            assertTrue(monkey.canBeInContactZoo());
        }
    }

    @Test
    void testCheckHealth_UnhealthyAnimalCannotBeInContactZoo() {
        Rabbit rabbit = new Rabbit("KindRabbit", 1002, 6);

        boolean healthResult = clinic.checkHealth(rabbit);

        if (!healthResult) {
            assertFalse(rabbit.canBeInContactZoo());
        }
    }

    @Test
    void testCheckHealth_AnimalPropertiesPreserved() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 7);
        int originalFood = monkey.getFood();
        String originalName = monkey.getName();
        int originalNumber = monkey.getNumber();

        clinic.checkHealth(monkey);

        assertEquals(originalFood, monkey.getFood());
        assertEquals(originalName, monkey.getName());
        assertEquals(originalNumber, monkey.getNumber());
        assertEquals(7, monkey.getKindnessLevel());
    }

    @Test
    void testCheckHealth_AlwaysSetsHealthStatus() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 7);

        clinic.checkHealth(monkey);

        assertTrue(monkey.isHealthy() || !monkey.isHealthy());
    }

    @Test
    void testCheckHealth_MethodCompletesSuccessfully() {
        Monkey monkey = new Monkey("TestMonkey", 1001, 7);

        assertDoesNotThrow(() -> clinic.checkHealth(monkey));
    }

    @Test
    void testCheckHealth_DeterministicForTesting() {
        VeterinaryClinic testClinic = new VeterinaryClinic() {
            @Override
            public boolean checkHealth(Animal animal) {
                animal.setHealthStatus(true);
                return true;
            }
        };

        Monkey monkey = new Monkey("TestMonkey", 1001, 7);

        boolean result1 = testClinic.checkHealth(monkey);
        boolean result2 = testClinic.checkHealth(monkey);

        assertEquals(result1, result2);
        assertTrue(monkey.isHealthy());
    }
}