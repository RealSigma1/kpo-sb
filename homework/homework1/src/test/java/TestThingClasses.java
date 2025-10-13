import com.zoo.entities.Computer;
import com.zoo.entities.Table;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestThingClasses {

    @Test
    void testTableCreation() {
        Table table = new Table("Office Table", 2001);

        assertEquals("Office Table", table.getName());
        assertEquals(2001, table.getNumber());
        assertTrue(table.getInventoryInfo().contains("Table"));
    }

    @Test
    void testComputerCreation() {
        Computer computer = new Computer("Admin PC", 2002);

        assertEquals("Admin PC", computer.getName());
        assertEquals(2002, computer.getNumber());
        assertTrue(computer.getInventoryInfo().contains("Computer"));
    }
}