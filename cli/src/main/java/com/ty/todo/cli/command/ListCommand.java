package com.ty.todo.cli.command;

import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;

import com.ty.todo.api.dto.TaskResponse;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Spec;

/**
 * Lists tasks fetched from the service API.
 */
@Command(name = "list", description = "List tasks")
public class ListCommand implements Callable<Integer> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @ParentCommand
    private TodoCommand parent;

    @Spec
    private CommandSpec spec;

    @Override
    public Integer call() {
        List<TaskResponse> tasks = parent.client().listTasks();
        PrintWriter out = spec.commandLine().getOut();
        if (tasks.isEmpty()) {
            out.println("No tasks found.");
            out.flush();
            return 0;
        }
        tasks.forEach(task -> out.printf("#%d | %s | remindAt=%s | remindedAt=%s | email=%s%n",
                task.id(),
                task.title(),
                format(task.remindAt()),
                format(task.remindedAt()),
                task.email()));
        out.flush();
        return 0;
    }

    private String format(java.time.LocalDateTime value) {
        return value == null ? "-" : FORMATTER.format(value);
    }
}
