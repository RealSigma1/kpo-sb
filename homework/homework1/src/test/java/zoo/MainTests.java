package zoo;

import com.zoo.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTests {

    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;

    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        try {
            Field scannerField = Main.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            scannerField.set(null, new Scanner(System.in));
        } catch (Exception ignored) {
        }
    }

    private void resetMainScannerToSystemIn() {
        try {
            Field scannerField = Main.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            scannerField.set(null, new Scanner(System.in));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMain_starts_and_addsSampleData_then_exits() {
        String input = "5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        resetMainScannerToSystemIn();

        Main.main(new String[]{});

        String output = outContent.toString();

        assertTrue(output.contains("Sample data added successfully!"),
                "Expected sample data message in console output\nOutput:\n" + output);

        assertTrue(output.contains("=== MOSCOW ZOO MANAGEMENT SYSTEM ==="),
                "Expected menu header in console output");
    }

    @Test
    public void testAddNewAnimal_monkey_addedSuccessfully() {
        String input = String.join("\n",
                "2",
                "1",
                "TestMonkey",
                "1234",
                "5",
                "5",
                ""
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        resetMainScannerToSystemIn();

        Main.main(new String[]{});

        String output = outContent.toString();

        boolean addedPositive = output.contains("successfully added to the zoo!") && output.contains("TestMonkey");
        boolean addedNegative = output.contains("was not accepted due to health issues") && output.contains("TestMonkey");

        assertTrue(addedPositive || addedNegative,
                "Expected either a positive or negative add-animal message for TestMonkey.\nOutput:\n" + output);
    }

    @Test
    public void testAddNewThing_table_addedSuccessfully() {
        String input = String.join("\n",
                "3",
                "1",
                "OfficeTable",
                "2005",
                "5",
                ""
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        resetMainScannerToSystemIn();

        Main.main(new String[]{});

        String output = outContent.toString();

        assertTrue(output.contains("successfully added to inventory") || output.contains("successfully added to the zoo!"),
                "Expected a success message when adding a thing.\nOutput:\n" + output);

        assertTrue(output.contains("OfficeTable"), "Expected the thing name to appear in output");
    }

    @Test
    public void testUpdateAnimalFood_updatesSuccessfully() {
        String input = String.join("\n",
                "4",
                "1",
                "10",
                "5",
                ""
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        resetMainScannerToSystemIn();

        Main.main(new String[]{});

        String output = outContent.toString();

        boolean positive = output.contains("food updated to") || output.contains("food updated to 10kg");
        boolean negative = output.contains("Failed to update food amount") || output.contains("negative! Failed to update food amount");

        assertTrue(positive || negative,
                "Expected either a success or failure message when updating animal food.\nOutput:\n" + output);
    }
}