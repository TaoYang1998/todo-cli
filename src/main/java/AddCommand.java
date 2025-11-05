import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

/**
 * Implements the `add` sub-command for creating new tasks.
 */
@Command(name = "add", description = "Add a task to the database")
public final class AddCommand implements java.util.concurrent.Callable<Integer> {
    @ParentCommand
    private Main parent;

    @Option(names = "--title", required = true, description = "Title of the task")
    private String title;

    @Option(names = "--at", description = "Reminder time in ISO-8601 format")
    private String at;

    @Option(names = "--email", description = "Email to notify for the reminder")
    private String email;

    @Override
    public Integer call() {
        var normalizedTitle = title == null ? "" : title.trim();
        if (normalizedTitle.isEmpty()) {
            System.err.println("Task title cannot be blank");
            return CommandLine.ExitCode.USAGE;
        }

        Optional<LocalDateTime> remindAt = Optional.empty();
        if (at != null && !at.isBlank()) {
            try {
                remindAt = Optional.of(LocalDateTime.parse(at.trim()));
            } catch (DateTimeParseException ex) {
                System.err.println("Invalid ISO-8601 date-time: " + at);
                return CommandLine.ExitCode.USAGE;
            }
        }

        Optional<String> emailAddress = Optional.ofNullable(email)
            .map(String::trim)
            .filter(item -> !item.isEmpty());

        var task = new Task(normalizedTitle, remindAt, emailAddress);
        try {
            parent.repo().add(task);
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return CommandLine.ExitCode.SOFTWARE;
        }
        System.out.println("Task added: " + task);
        return CommandLine.ExitCode.OK;
    }
}
