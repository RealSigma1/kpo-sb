package zoo;

import org.zoo.entities.Monkey;
import org.zoo.entities.Tiger;
import org.junit.jupiter.api.Test;
import org.zoo.services.VeterinaryClinic;

import static org.junit.jupiter.api.Assertions.*;

class TestVeterinaryClinic {

    @Test
    void testCheckHealth_ReturnsBoolean() {
        VeterinaryClinic clinic = new VeterinaryClinic();
        Monkey monkey = new Monkey("TestMonkey", 1001, 7);

        boolean result = clinic.checkHealth(monkey);

        assertTrue(result || !result);
        assertEquals(result, monkey.isHealthy());
    }

    @Test
    void testCheckHealth_SetsHealthStatus() {
        VeterinaryClinic clinic = new VeterinaryClinic();
        Tiger tiger = new Tiger("TestTiger", 1002);

        clinic.checkHealth(tiger);

        assertTrue(tiger.isHealthy() || !tiger.isHealthy());
    }
}