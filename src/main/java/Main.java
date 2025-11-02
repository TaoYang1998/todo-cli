import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Entry point that wires the todo-cli commands together.
 */
@Command(
    name = "todo-cli",
    description = "A simple todo list CLI backed by an in-memory store",
    mixinStandardHelpOptions = true,
    version = "todo-cli 0.1"
)
public final class Main implements java.util.concurrent.Callable<Integer> {
    private final List<Task> tasks = new CopyOnWriteArrayList<>();

    @Override
    public Integer call() {
        System.out.println("Specify a subcommand (add | list). Use --help for details.");
        return CommandLine.ExitCode.OK;
    }

    public static void main(String[] args) {
        var main = new Main();
        var commandLine = new CommandLine(main);
        commandLine.addSubcommand("add", new AddCommand(main.tasks));
        commandLine.addSubcommand("list", new ListCommand(main.tasks));
        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }
}
