package org.zoo.services;

import org.zoo.entities.Animal;

public class VeterinaryClinic {
    public boolean checkHealth(Animal animal) {
        boolean isHealthy = Math.random() > 0.2;
        animal.setHealthStatus(isHealthy);
        return isHealthy;
    }
}