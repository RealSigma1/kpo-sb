import com.zoo.container.ServiceContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceContainerTest {

    @Test
    void testRegisterAndGetService() {
        ServiceContainer container = new ServiceContainer();
        String testService = "Test Service";

        container.registerSingleton(String.class, testService);
        String result = container.getService(String.class);

        assertEquals(testService, result);
    }

    @Test
    void testGetServiceNotRegistered() {
        ServiceContainer container = new ServiceContainer();

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            container.getService(String.class);
        });

        assertTrue(exception.getMessage().contains("Service not registered"));
    }

    @Test
    void testOverrideService() {
        ServiceContainer container = new ServiceContainer();

        container.registerSingleton(String.class, "First");
        container.registerSingleton(String.class, "Second");

        String result = container.getService(String.class);
        assertEquals("Second", result);
    }
}