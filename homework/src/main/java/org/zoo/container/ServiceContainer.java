package org.zoo.container;

import java.util.HashMap;
import java.util.Map;

public class ServiceContainer {
    private Map<Class<?>, Object> services = new HashMap<>();

    public <T> void registerSingleton(Class<T> serviceClass, T instance) {
        services.put(serviceClass, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        Object service = services.get(serviceClass);
        if (service == null) {
            throw new IllegalStateException("Service not registered: " + serviceClass.getName());
        }
        return (T) service;
    }
}