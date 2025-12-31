package com.fueltracker.backend.service;

import com.fueltracker.backend.exception.ResourceNotFoundException;
import com.fueltracker.backend.model.Car;
import com.fueltracker.backend.model.FuelEntry;
import com.fueltracker.backend.model.FuelStats;
import com.fueltracker.backend.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service layer for managing cars and fuel entries.
 */
@Service
@RequiredArgsConstructor
public class CarService {
    
    private final CarRepository carRepository;
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Creates a new car and stores it.
     */
    public Car createCar(String brand, String model, Integer year) {
        Long id = idGenerator.getAndIncrement();
        Car car = new Car(id, brand, model, year);
        return carRepository.save(car);
    }

    /**
     * Retrieves all cars.
     */
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    /**
     * Finds a car by its ID.
     */
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    /**
     * Adds a fuel entry to a specific car.
     */
    public FuelEntry addFuelEntry(Long carId, Double liters, Double price, Integer odometer) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + carId + " not found"));

        FuelEntry entry = new FuelEntry(liters, price, odometer);
        car.addFuelEntry(entry);
        return entry;
    }

    /**
     * Calculates fuel statistics for a specific car.
     */
    public FuelStats calculateFuelStats(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + carId + " not found"));

        List<FuelEntry> entries = car.getFuelEntries();
        
        if (entries.isEmpty()) {
            return new FuelStats(0.0, 0.0, 0.0);
        }

        double totalFuel = 0.0;
        double totalCost = 0.0;

        for (FuelEntry entry : entries) {
            totalFuel += entry.getLiters();
            totalCost += entry.getPrice();
        }

        // Calculate average consumption per 100km
        double averageConsumption = calculateAverageConsumption(entries);

        return new FuelStats(totalFuel, totalCost, averageConsumption);
    }

    /**
     * Calculates average fuel consumption per 100km.
     * Uses the difference between first and last odometer readings.
     */
    private double calculateAverageConsumption(List<FuelEntry> entries) {
        if (entries.size() < 2) {
            return 0.0;
        }

        // Sort by odometer to get first and last readings
        List<FuelEntry> sorted = new ArrayList<>(entries);
        sorted.sort(Comparator.comparingInt(FuelEntry::getOdometer));

        int firstOdometer = sorted.get(0).getOdometer();
        int lastOdometer = sorted.get(sorted.size() - 1).getOdometer();
        int totalDistance = lastOdometer - firstOdometer;

        if (totalDistance == 0) {
            return 0.0;
        }

        double totalFuel = entries.stream()
                .mapToDouble(FuelEntry::getLiters)
                .sum();

        // Convert to L/100km
        return (totalFuel / totalDistance) * 100;
    }
}