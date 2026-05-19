package com.sptech.school.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

public class Jira {
    private final String baseUrl = Dotenv.configure().filename(".env.dev").load().get("JIRA_BASE_URL");
    private final String authHeader;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiToken = Dotenv.configure().filename(".env.dev").load().get("JIRA_API_TOKEN");
    private final String email = Dotenv.configure().filename(".env.dev").load().get("JIRA_EMAIL");

    public Jira() {

        String auth = email + ":" + apiToken;
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();

        this.objectMapper = new ObjectMapper();
    }

    private HttpRequest.Builder baseRequest() {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/rest/api/3/issue"))
                .timeout(Duration.ofSeconds(60))
                .header("Authorization", authHeader)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    private String sendRequest(HttpRequest request) throws Exception {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();

        if (status >= 200 && status < 300) {
            return response.body();
        }

        throw new RuntimeException("Jira request failed: " + status + " - " + response.body());
    }

    public String createIssue(String projectKey, String summary, String issueType, String priority) throws Exception {
        Map<String, Object> payload = Map.of(
                "fields",
                Map.of(
                        "project",
                        Map.of("key", projectKey),
                        "summary",
                        summary,
                        "issuetype",
                        Map.of("name", issueType),
                        "priority",
                        Map.of("name", priority)
                )
        );

        String json = objectMapper.writeValueAsString(payload);

        HttpRequest request = baseRequest()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return sendRequest(request);
    }
}
