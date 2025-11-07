package com.ty.todo.cli.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.ty.todo.api.dto.TaskRequest;
import com.ty.todo.api.dto.TaskResponse;
import com.ty.todo.cli.client.TodoServiceClient;
import com.ty.todo.cli.client.TodoServiceClientFactory;

import picocli.CommandLine;

class AddCommandTest {

    @Test
    void addCommandSendsRequest() {
        CapturingClient client = new CapturingClient();
        TodoServiceClientFactory factory = baseUrl -> client;
        CommandLine commandLine = new CommandLine(new TodoCommand(factory));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        commandLine.setOut(new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8), true));

        int exit = commandLine.execute("add", "--title", "CLI Test", "--remind-at", "2025-11-07T10:00:00", "--email", "cli@example.com");

        assertThat(exit).isEqualTo(0);
        assertThat(client.lastRequest).isNotNull();
        assertThat(client.lastRequest.title()).isEqualTo("CLI Test");
        assertThat(client.lastRequest.remindAt()).isEqualTo(LocalDateTime.of(2025, 11, 7, 10, 0));
        assertThat(client.lastRequest.email()).isEqualTo("cli@example.com");
        assertThat(baos.toString(StandardCharsets.UTF_8)).contains("Created task");
    }

    private static final class CapturingClient implements TodoServiceClient {

        private TaskRequest lastRequest;

        @Override
        public TaskResponse createTask(TaskRequest request) {
            this.lastRequest = request;
            return new TaskResponse(1L, request.title(), request.remindAt(), request.email(), null);
        }

        @Override
        public List<TaskResponse> listTasks() {
            return List.of();
        }
    }
}
