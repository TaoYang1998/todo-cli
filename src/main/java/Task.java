import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a todo task in the in-memory store.
 */
public record Task(String title, Optional<LocalDateTime> remindAt, Optional<String> email) {

    public Task {
        Objects.requireNonNull(title, "title must not be null");
        title = title.trim();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        email = email.isEmpty()
                ? Optional.empty()
                : email.map(String::trim).filter(item -> !item.isEmpty());
    }

    @Override
    public String toString() {
        var builder = new StringBuilder(title);
        remindAt.ifPresent(time -> builder.append(" @ ").append(time));
        email.ifPresent(addr -> builder.append(" -> ").append(addr));
        return builder.toString();
    }
}
