# Car Fuel Tracker

A simple system to track car fuel consumption, consisting of a Spring Boot backend and a Java CLI client.

## Prerequisites

- Java 8 or higher
- Maven

## How to Run

### 1. Start the Backend

Open a terminal and run:

```bash
mvn spring-boot:run -pl backend
```

The server will start on `http://localhost:8084`.

### 2. Run the CLI Client

Open a new terminal window and use the provided helper script:

```bash
# Create a car
./fuel-cli.sh create-car --brand BYD --model Atto-3 --year 2025


# Add fuel - Use the ID returned from create-car
./fuel-cli.sh add-fuel --carId 1 --liters 45.5 --price 1.50 --odometer 10500

# View stats
./fuel-cli.sh fuel-stats --carId 1
```

## Project Structure

- `backend`: Spring Boot application with in-memory storage
- `cli-client`: Java Command Line Interface

## Alternative Access Methods

### Standard HTTP Requests (Normal POST)
You can interact with the API using standard tools like `curl`:

**Create Car:**
```bash
curl -X POST http://localhost:8084/api/cars \
     -H "Content-Type: application/json" \
     -d '{"brand":"Honda", "model":"Civic", "year":2022}'
```

**Add Fuel:**
```bash
curl -X POST http://localhost:8084/api/cars/1/fuel \
     -H "Content-Type: application/json" \
     -d '{"liters":30.0, "price":1.45, "odometer":12000}'
```

**View Fuel Stats:**
```bash
curl http://localhost:8084/api/cars/1/fuel/stats
```

### Raw Servlet Access
The application also exposes a raw Servlet endpoint to list cars:

```bash
curl "http://localhost:8084/servlet/fuel-stats?carId=1"
```
