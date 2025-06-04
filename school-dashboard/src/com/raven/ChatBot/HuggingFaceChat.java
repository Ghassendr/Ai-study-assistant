package com.raven.ChatBot;

import org.json.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HuggingFaceChat {
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api-inference.huggingface.co/models/google/flan-t5-base";

    public static String sendMessage(String message) {
        try {
            // Build the JSON request for Hugging Face
            JSONObject json = new JSONObject()
                    .put("inputs", message);

            // Create HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Send request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            JSONArray jsonResponse = new JSONArray(response.body());
            if (jsonResponse.length() > 0) {
                JSONObject firstResponse = jsonResponse.getJSONObject(0);
                return firstResponse.getString("generated_text").trim();
            }
            return "Error: No response from model";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}