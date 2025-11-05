import java.util.List;

/**
 * Repository abstraction for persisting and querying tasks.
 */
public interface TaskRepository {
    void add(Task task);
    List<Task> list();
}