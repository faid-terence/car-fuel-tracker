package com.fueltracker.backend;

import com.fueltracker.backend.exception.ResourceNotFoundException;
import com.fueltracker.backend.model.Car;
import com.fueltracker.backend.model.FuelEntry;
import com.fueltracker.backend.model.FuelStats;
import com.fueltracker.backend.service.CarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerificationTest {

    private CarService carService;
    private com.fueltracker.backend.repository.CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new com.fueltracker.backend.repository.CarRepository();
        carService = new CarService(carRepository);
    }

    @Test
    void testLombokIntegration() {
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        
        assertEquals("Toyota", car.getBrand());
        assertEquals("Corolla", car.getModel());

        FuelEntry entry = new FuelEntry(10.0, 15.0, 1000);
        assertEquals(10.0, entry.getLiters());
    }

    @Test
    void testResourceNotFoundException_AddFuel() {
        assertThrows(ResourceNotFoundException.class, () -> {
            carService.addFuelEntry(999L, 10.0, 10.0, 1000);
        });
    }

    @Test
    void testResourceNotFoundException_GetStats() {
        assertThrows(ResourceNotFoundException.class, () -> {
            carService.calculateFuelStats(999L);
        });
    }

    @Test
    void testSuccessfulFlow() {
        Car car = carService.createCar("Honda", "Civic", 2020);
        assertNotNull(car.getId());
        
        carService.addFuelEntry(car.getId(), 50.0, 100.0, 1000);
        carService.addFuelEntry(car.getId(), 40.0, 90.0, 1500); // 500km diff

        FuelStats stats = carService.calculateFuelStats(car.getId());
        assertEquals(90.0, stats.getTotalFuel());
        assertEquals(190.0, stats.getTotalCost());
        // 90L / 500km * 100 = 18L/100km
        assertEquals(18.0, stats.getAverageConsumptionPer100Km(), 0.01);
    }
}
