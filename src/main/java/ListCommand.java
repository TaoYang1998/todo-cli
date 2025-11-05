import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

/**
 * Implements the `list` sub-command that prints all tasks.
 */
@Command(name = "list", description = "List all current tasks from database")
public final class ListCommand implements java.util.concurrent.Callable<Integer> {
    @ParentCommand
    private Main parent;

    @Override
    public Integer call() {
        try {
            var tasks = parent.repo().list();
            if (tasks.isEmpty()) {
                System.out.println("No tasks found. Use `add` to create one.");
            } else {
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return CommandLine.ExitCode.SOFTWARE;
        }
        return CommandLine.ExitCode.OK;
    }
}
