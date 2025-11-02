import java.util.List;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Implements the `list` sub-command that prints all tasks.
 */
@Command(name = "list", description = "List all current tasks")
public final class ListCommand implements java.util.concurrent.Callable<Integer> {
    private final List<Task> tasks;

    public ListCommand(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Integer call() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found. Use `add` to create one.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
        return CommandLine.ExitCode.OK;
    }
}
