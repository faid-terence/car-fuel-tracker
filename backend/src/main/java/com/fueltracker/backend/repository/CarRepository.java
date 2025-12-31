package com.fueltracker.backend.repository;

import com.fueltracker.backend.model.Car;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CarRepository {

    private final Map<Long, Car> carStorage = new HashMap<>();

    public Car save(Car car) {
        carStorage.put(car.getId(), car);
        return car;
    }

    public Optional<Car> findById(Long id) {
        return Optional.ofNullable(carStorage.get(id));
    }

    public List<Car> findAll() {
        return new ArrayList<>(carStorage.values());
    }
}
