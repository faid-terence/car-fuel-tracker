package com.fueltracker.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a fuel entry for a car.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelEntry {
    private Double liters;
    private Double price;
    private Integer odometer;
}
