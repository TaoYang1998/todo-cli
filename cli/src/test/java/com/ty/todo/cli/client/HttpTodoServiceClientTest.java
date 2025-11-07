package com.ty.todo.cli.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ty.todo.api.dto.TaskRequest;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

class HttpTodoServiceClientTest {

    private MockWebServer server;
    private HttpTodoServiceClient client;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        client = new HttpTodoServiceClient(server.url("/").toString(), JsonSupport.mapper());
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    void createTaskPostsJson() throws InterruptedException {
        server.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody("{" +
                        "\"id\":1," +
                        "\"title\":\"Test\"," +
                        "\"remindAt\":\"2025-11-07T10:00:00\"," +
                        "\"email\":\"user@example.com\"," +
                        "\"remindedAt\":null" +
                        "}"));

        client.createTask(new TaskRequest("Test", LocalDateTime.of(2025, 11, 7, 10, 0), "user@example.com"));

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/api/tasks");
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/json");
    }

    @Test
    void listTasksHandlesErrors() {
        server.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("boom"));

        assertThatThrownBy(() -> client.listTasks())
                .isInstanceOf(TodoServiceClientException.class)
                .hasMessageContaining("500");
    }
}
