package com.fueltracker.backend.controller;

import com.fueltracker.backend.dto.AddFuelRequest;
import com.fueltracker.backend.dto.CreateCarRequest;
import com.fueltracker.backend.model.Car;
import com.fueltracker.backend.model.FuelEntry;
import com.fueltracker.backend.model.FuelStats;
import com.fueltracker.backend.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller for car and fuel management.
 */
@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * Creates a new car.
     * POST /api/cars
     */
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody @Valid CreateCarRequest request) {
        Car car = carService.createCar(request.getBrand(), request.getModel(), request.getYear());
        return ResponseEntity.status(HttpStatus.CREATED).body(car);
    }

    /**
     * Lists all cars.
     * GET /api/cars
     */
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    /**
     * Adds a fuel entry to a specific car.
     * POST /api/cars/{id}/fuel
     */
    @PostMapping("/{id}/fuel")
    public ResponseEntity<FuelEntry> addFuelEntry(
            @PathVariable Long id,
            @RequestBody @Valid AddFuelRequest request) {
        
        FuelEntry entry = carService.addFuelEntry(id, request.getLiters(), request.getPrice(), request.getOdometer());
        return ResponseEntity.status(HttpStatus.CREATED).body(entry);
    }

    /**
     * Gets fuel statistics for a specific car.
     * GET /api/cars/{id}/fuel/stats
     */
    @GetMapping("/{id}/fuel/stats")
    public ResponseEntity<FuelStats> getFuelStats(@PathVariable Long id) {
        FuelStats stats = carService.calculateFuelStats(id);
        return ResponseEntity.ok(stats);
    }
}