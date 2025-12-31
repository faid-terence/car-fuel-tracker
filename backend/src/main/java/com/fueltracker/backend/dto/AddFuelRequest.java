package com.fueltracker.backend.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AddFuelRequest {
    @NotNull(message = "Liters is required")
    @Min(value = 0, message = "Liters must be positive")
    private Double liters;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @NotNull(message = "Odometer is required")
    @Min(value = 0, message = "Odometer must be positive")
    private Integer odometer;
}
