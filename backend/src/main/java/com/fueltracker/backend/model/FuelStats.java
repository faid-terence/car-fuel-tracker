package com.fueltracker.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelStats {
    private Double totalFuel;
    private Double totalCost;
    private Double averageConsumptionPer100Km;
}