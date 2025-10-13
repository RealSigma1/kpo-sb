import com.zoo.entities.Monkey;
import com.zoo.entities.Table;
import com.zoo.interfaces.IAlive;
import com.zoo.interfaces.IInventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestInterfaces {

    @Test
    void testIAliveImplementation() {
        IAlive monkey = new Monkey("Test", 1001, 6);

        assertEquals("Test", monkey.getName());
        assertTrue(monkey.getFood() > 0);
    }

    @Test
    void testIInventoryImplementation() {
        IInventory table = new Table("TestTable", 2001);
        IInventory monkey = new Monkey("TestMonkey", 1002, 6);

        assertTrue(table.getNumber() > 0);
        assertTrue(monkey.getNumber() > 0);
        assertNotNull(table.getInventoryInfo());
        assertNotNull(monkey.getInventoryInfo());
    }
}