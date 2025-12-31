package com.fueltracker.cli.model;

public enum Command {
    CREATE_CAR("create-car"),
    ADD_FUEL("add-fuel"),
    FUEL_STATS("fuel-stats");

    private final String value;

    Command(String value) {
        this.value = value;
    }

    public static Command fromString(String text) {
        for (Command b : Command.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unknown command: " + text);
    }
}
