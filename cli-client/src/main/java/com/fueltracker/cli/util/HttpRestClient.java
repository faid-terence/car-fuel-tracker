package com.fueltracker.cli.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpRestClient {

    public String sendPostRequest(String urlString, String jsonBody) throws Exception {
        return sendRequest(urlString, "POST", jsonBody);
    }

    public String sendGetRequest(String urlString) throws Exception {
        return sendRequest(urlString, "GET", null);
    }

    private String sendRequest(String urlString, String method, String jsonBody) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod(method);
            if (jsonBody != null) {
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
                }
            }

            int responseCode = conn.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                return readResponse(conn);
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                System.err.println("Error: Resource not found (404)");
                return null;
            } else {
                System.err.println("Request failed with status: " + responseCode);
                printErrorDetails(conn);
                return null;
            }
        } finally {
            conn.disconnect();
        }
    }

    private String readResponse(HttpURLConnection conn) throws Exception {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private void printErrorDetails(HttpURLConnection conn) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                errorResponse.append(line);
            }
            if (errorResponse.length() > 0) {
                System.err.println("Error details: " + errorResponse.toString());
            }
        } catch (Exception e) {
            // Ignore error reading error stream
        }
    }
}
