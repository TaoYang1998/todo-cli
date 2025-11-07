package com.ty.todo.cli.command;

import com.ty.todo.cli.client.HttpTodoServiceClientFactory;
import com.ty.todo.cli.client.TodoServiceClient;
import com.ty.todo.cli.client.TodoServiceClientFactory;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

/**
 * Root Picocli command that wires sub-commands.
 */
@Command(
        name = "todo",
        mixinStandardHelpOptions = true,
        description = "Command line client for the todo-service",
        subcommands = {
                AddCommand.class,
                ListCommand.class
        })
public class TodoCommand implements Runnable {

    @Option(names = {"-b", "--base-url"},
            defaultValue = "${env:TODO_SERVICE_BASE_URL:-http://localhost:8080}",
            description = "Base URL of the todo-service (default: http://localhost:8080 or TODO_SERVICE_BASE_URL)")
    String baseUrl;

    private final TodoServiceClientFactory clientFactory;
    private TodoServiceClient cachedClient;

    @Spec
    CommandSpec spec;

    public TodoCommand() {
        this(new HttpTodoServiceClientFactory());
    }

    TodoCommand(TodoServiceClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void run() {
        spec.commandLine().usage(spec.commandLine().getOut());
    }

    public TodoServiceClient client() {
        if (cachedClient == null) {
            cachedClient = clientFactory.create(baseUrl);
        }
        return cachedClient;
    }
}
