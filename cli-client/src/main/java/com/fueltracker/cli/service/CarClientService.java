package com.fueltracker.cli.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fueltracker.cli.dto.AddFuelRequest;
import com.fueltracker.cli.dto.CreateCarRequest;
import com.fueltracker.cli.util.ArgParser;
import com.fueltracker.cli.util.HttpRestClient;

import java.util.Map;
import java.util.ResourceBundle;

public class CarClientService {

    private final String baseUrl;
    private final ObjectMapper objectMapper;
    private final HttpRestClient httpClient;

    public CarClientService() {
        this.baseUrl = ResourceBundle.getBundle("application").getString("app.base-url");
        this.objectMapper = new ObjectMapper();
        this.httpClient = new HttpRestClient();
    }

    public void handleCreateCar(String[] args) throws Exception {
        Map<String, String> params = ArgParser.parseArguments(args);
        ArgParser.validateRequiredParams(params, "brand", "model", "year");

        String brand = params.get("brand");
        String model = params.get("model");
        int year = Integer.parseInt(params.get("year"));

        CreateCarRequest request = new CreateCarRequest(brand, model, year);
        String jsonBody = objectMapper.writeValueAsString(request);

        // Send POST request
        String response = httpClient.sendPostRequest(baseUrl + "/api/cars", jsonBody);

        if (response != null) {
            JsonNode car = objectMapper.readTree(response);
            System.out.println("Car created successfully!");
            System.out.println("Car ID: " + car.get("id").asText());
            System.out.println("Brand: " + car.get("brand").asText());
            System.out.println("Model: " + car.get("model").asText());
            System.out.println("Year: " + car.get("year").asText());
        }
    }

    public void handleAddFuel(String[] args) throws Exception {
        Map<String, String> params = ArgParser.parseArguments(args);
        ArgParser.validateRequiredParams(params, "carId", "liters", "price", "odometer");

        String carId = params.get("carId");
        double liters = Double.parseDouble(params.get("liters"));
        double price = Double.parseDouble(params.get("price"));
        int odometer = Integer.parseInt(params.get("odometer"));

        AddFuelRequest request = new AddFuelRequest(liters, price, odometer);
        String jsonBody = objectMapper.writeValueAsString(request);

        // Send POST request
        String response = httpClient.sendPostRequest(baseUrl + "/api/cars/" + carId + "/fuel", jsonBody);

        if (response != null) {
            System.out.println("Fuel entry added successfully!");
            JsonNode entry = objectMapper.readTree(response);
            System.out.println("Liters: " + entry.get("liters").asDouble());
            System.out.println("Price: " + entry.get("price").asDouble());
            System.out.println("Odometer: " + entry.get("odometer").asInt());
        }
    }

    public void handleFuelStats(String[] args) throws Exception {
        Map<String, String> params = ArgParser.parseArguments(args);
        ArgParser.validateRequiredParams(params, "carId");

        String carId = params.get("carId");

        // Send GET request
        String response = httpClient.sendGetRequest(baseUrl + "/api/cars/" + carId + "/fuel/stats");

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
}
