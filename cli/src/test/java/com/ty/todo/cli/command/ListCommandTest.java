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

class ListCommandTest {

    @Test
    void listCommandPrintsTasks() {
        TodoServiceClient client = new TodoServiceClient() {
            @Override
            public TaskResponse createTask(TaskRequest request) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<TaskResponse> listTasks() {
                return List.of(new TaskResponse(5L, "Sample", LocalDateTime.of(2025, 11, 7, 10, 0), "me@example.com", null));
            }
        };
        TodoServiceClientFactory factory = baseUrl -> client;
        CommandLine commandLine = new CommandLine(new TodoCommand(factory));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        commandLine.setOut(new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8), true));

        int exit = commandLine.execute("list");

        assertThat(exit).isEqualTo(0);
        assertThat(baos.toString(StandardCharsets.UTF_8)).contains("#5");
    }
}
