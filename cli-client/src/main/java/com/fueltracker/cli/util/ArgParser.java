package com.fueltracker.cli.util;

import java.util.HashMap;
import java.util.Map;

public class ArgParser {

    public static Map<String, String> parseArguments(String[] args) {
        Map<String, String> params = new HashMap<>();

        for (int i = 1; i < args.length; i += 2) {
            if (args[i].startsWith("--") && i + 1 < args.length) {
                String key = args[i].substring(2);
                String value = args[i + 1];
                params.put(key, value);
            }
        }

        return params;
    }

    public static void validateRequiredParams(Map<String, String> params, String... requiredKeys) throws IllegalArgumentException {
        for (String key : requiredKeys) {
            if (!params.containsKey(key) || params.get(key) == null) {
                StringBuilder sb = new StringBuilder("Missing required parameters: ");
                for (String k : requiredKeys) {
                    sb.append("--").append(k).append(" ");
                }
                throw new IllegalArgumentException(sb.toString().trim());
            }
        }
    }
}
