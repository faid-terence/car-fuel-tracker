package com.fueltracker.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fueltracker.cli.util.ArgParser;
import com.fueltracker.cli.util.HttpRestClient;

import java.util.HashMap;
import java.util.Map;

/**
 * CLI Client for interacting with the Fuel Tracker API.
 * Java 8 compatible version using HttpURLConnection.
 */
public class CliApplication {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpRestClient httpClient = new HttpRestClient();

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            String command = args[0];

            switch (command) {
                case "create-car":
                    handleCreateCar(args);
                    break;
                case "add-fuel":
                    handleAddFuel(args);
                    break;
                case "fuel-stats":
                    handleFuelStats(args);
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    printUsage();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles create-car command.
     */
    private static void handleCreateCar(String[] args) throws Exception {
        Map<String, String> params = ArgParser.parseArguments(args);
        ArgParser.validateRequiredParams(params, "brand", "model", "year");

        String brand = params.get("brand");
        String model = params.get("model");
        int year = Integer.parseInt(params.get("year"));

        // Build JSON request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("brand", brand);
        requestBody.put("model", model);
        requestBody.put("year", year);

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        // Send POST request
        String response = httpClient.sendPostRequest(BASE_URL + "/api/cars", jsonBody);

        if (response != null) {
            JsonNode car = objectMapper.readTree(response);
            System.out.println("Car created successfully!");
            System.out.println("Car ID: " + car.get("id").asText());
            System.out.println("Brand: " + car.get("brand").asText());
            System.out.println("Model: " + car.get("model").asText());
            System.out.println("Year: " + car.get("year").asText());
        }
    }

    /**
     * Handles add-fuel command.
     */
    private static void handleAddFuel(String[] args) throws Exception {
        Map<String, String> params = ArgParser.parseArguments(args);
        ArgParser.validateRequiredParams(params, "carId", "liters", "price", "odometer");

        String carId = params.get("carId");
        double liters = Double.parseDouble(params.get("liters"));
        double price = Double.parseDouble(params.get("price"));
        int odometer = Integer.parseInt(params.get("odometer"));

        // Build JSON request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("liters", liters);
        requestBody.put("price", price);
        requestBody.put("odometer", odometer);

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        // Send POST request
        String response = httpClient.sendPostRequest(BASE_URL + "/api/cars/" + carId + "/fuel", jsonBody);

        if (response != null) {
            System.out.println("Fuel entry added successfully!");
            JsonNode entry = objectMapper.readTree(response);
            System.out.println("Liters: " + entry.get("liters").asDouble());
            System.out.println("Price: " + entry.get("price").asDouble());
            System.out.println("Odometer: " + entry.get("odometer").asInt());
        }
    }

    /**
     * Handles fuel-stats command.
     */
    private static void handleFuelStats(String[] args) throws Exception {
        Map<String, String> params = ArgParser.parseArguments(args);
        ArgParser.validateRequiredParams(params, "carId");

        String carId = params.get("carId");

        // Send GET request
        String response = httpClient.sendGetRequest(BASE_URL + "/api/cars/" + carId + "/fuel/stats");

        if (response != null) {
            JsonNode stats = objectMapper.readTree(response);
            System.out.println("=== Fuel Statistics ===");
            System.out.printf("Total fuel: %.2f L%n",
                    stats.get("totalFuel").asDouble());
            System.out.printf("Total cost: %.2f%n",
                    stats.get("totalCost").asDouble());
            System.out.printf("Average consumption: %.2f L/100km%n",
                    stats.get("averageConsumptionPer100Km").asDouble());
        }
    }

    /**
     * Prints usage instructions.
     */
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  create-car --brand <brand> --model <model> --year <year>");
        System.out.println("  add-fuel --carId <id> --liters <liters> --price <price> --odometer <odometer>");
        System.out.println("  fuel-stats --carId <id>");
    }
}