import com.zoo.Main;
import com.zoo.container.ServiceContainer;
import com.zoo.services.VeterinaryClinic;
import com.zoo.services.ZooService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MainTest {

    @Test
    void testServiceContainerIntegration() {
        ServiceContainer container = new ServiceContainer();
        container.registerSingleton(VeterinaryClinic.class, new VeterinaryClinic());
        container.registerSingleton(ZooService.class,
                new ZooService(container.getService(VeterinaryClinic.class)));

        ZooService zooService = container.getService(ZooService.class);
        assertNotNull(zooService);
    }

    @Test
    void testMainClassExists() {
        Main main = new Main();
        assertNotNull(main);
    }
}