package com.fueltracker.cli;

import com.fueltracker.cli.model.Command;
import com.fueltracker.cli.service.CarClientService;

public class CliApplication {

    private static final CarClientService carService = new CarClientService();

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            Command command = Command.fromString(args[0]);

            switch (command) {
                case CREATE_CAR:
                    carService.handleCreateCar(args);
                    break;
                case ADD_FUEL:
                    carService.handleAddFuel(args);
                    break;
                case FUEL_STATS:
                    carService.handleFuelStats(args);
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  create-car --brand <brand> --model <model> --year <year>");
        System.out.println("  add-fuel --carId <id> --liters <liters> --price <price> --odometer <odometer>");
        System.out.println("  fuel-stats --carId <id>");
    }
}