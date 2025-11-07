package com.ty.todo.cli.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.todo.api.dto.TaskRequest;
import com.ty.todo.api.dto.TaskResponse;

/**
 * HTTP implementation backed by Java's HttpClient.
 */
public class HttpTodoServiceClient implements TodoServiceClient {

    private static final TypeReference<List<TaskResponse>> TASK_LIST_REF = new TypeReference<>() {
    };

    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public HttpTodoServiceClient(String baseUrl, ObjectMapper mapper) {
        this.baseUrl = normalizeBaseUrl(baseUrl);
        this.mapper = mapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    @Override
    public TaskResponse createTask(TaskRequest request) {
        try {
            String payload = mapper.writeValueAsString(request);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/tasks"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ensureSuccess(response);
            return mapper.readValue(response.body(), TaskResponse.class);
        } catch (IOException ex) {
            throw new TodoServiceClientException("Failed to serialize or deserialize payload", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new TodoServiceClientException("HTTP call interrupted", ex);
        }
    }

    @Override
    public List<TaskResponse> listTasks() {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/tasks"))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ensureSuccess(response);
            return mapper.readValue(response.body(), TASK_LIST_REF);
        } catch (IOException ex) {
            throw new TodoServiceClientException("Failed to process response", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new TodoServiceClientException("HTTP call interrupted", ex);
        }
    }

    private void ensureSuccess(HttpResponse<String> response) {
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return;
        }
        throw new TodoServiceClientException("Service responded with status " + response.statusCode() + ": " + response.body());
    }

    private String normalizeBaseUrl(String raw) {
        if (raw == null || raw.isBlank()) {
            return "http://localhost:8080";
        }
        return raw.endsWith("/") ? raw.substring(0, raw.length() - 1) : raw;
    }
}
