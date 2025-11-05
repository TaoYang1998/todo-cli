import java.util.Objects;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Entry point that wires the todo-cli commands together.
 */
@Command(
    name = "todo-cli",
    description = "A simple todo list CLI backed by MyBatis/MySQL",
    mixinStandardHelpOptions = true,
    version = "todo-cli 0.2"
)
public final class Main implements java.util.concurrent.Callable<Integer> {
    private final TaskRepository repository;

    public Main() {
        var url = envOrDefault("TODO_DB_URL",
            "jdbc:mysql://localhost:3306/todo_cli?createDatabaseIfNotExist=true&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true");
        var user = envOrDefault("TODO_DB_USER", "root");
        var pass = envOrDefault("TODO_DB_PASS", "");
        this.repository = new MyBatisTaskRepository(url, user, pass);
    }

    public TaskRepository repo() { return repository; }

    private static String envOrDefault(String key, String def) {
        var v = System.getenv(key);
        return v == null || v.isBlank() ? def : v.trim();
    }

    @Override
    public Integer call() {
        System.out.println("Specify a subcommand (add | list). Use --help for details.");
        return CommandLine.ExitCode.OK;
    }

    public static void main(String[] args) {
        var main = new Main();
        var commandLine = new CommandLine(main);
        commandLine.addSubcommand("add", new AddCommand());
        commandLine.addSubcommand("list", new ListCommand());
        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }
}
