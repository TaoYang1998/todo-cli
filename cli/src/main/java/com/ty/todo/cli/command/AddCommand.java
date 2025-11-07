package com.ty.todo.cli.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.concurrent.Callable;

import com.ty.todo.api.dto.TaskRequest;
import com.ty.todo.api.dto.TaskResponse;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Spec;

/**
 * Adds a new task via the service API.
 */
@Command(name = "add", description = "Add a new todo item")
public class AddCommand implements Callable<Integer> {

    @ParentCommand
    private TodoCommand parent;

    @Spec
    private CommandSpec spec;

    @Option(names = {"-t", "--title"}, required = true, description = "Title of the task")
    private String title;

    @Option(names = {"-r", "--remind-at"}, description = "ISO-8601 timestamp (e.g. 2025-11-07T09:00:00)")
    private String remindAt;

    @Option(names = {"-e", "--email"}, description = "Email to notify")
    private String email;

    @Override
    public Integer call() {
        LocalDateTime remindTime = parseRemindAt();
        TaskRequest request = new TaskRequest(title, remindTime, email);
        TaskResponse response = parent.client().createTask(request);
        spec.commandLine().getOut().printf("Created task #%d: %s%n", response.id(), response.title());
        spec.commandLine().getOut().flush();
        return 0;
    }

    private LocalDateTime parseRemindAt() {
        if (remindAt == null || remindAt.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(remindAt.trim());
        } catch (DateTimeParseException ex) {
            throw new ParameterException(spec.commandLine(), "Invalid --remind-at value. Use ISO-8601, e.g. 2025-11-07T09:00:00");
        }
    }
}
