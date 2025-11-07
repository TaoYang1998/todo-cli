package com.ty.todo.task.reminder;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ty.todo.task.domain.Task;
import com.ty.todo.task.persistence.TaskEntity;
import com.ty.todo.task.persistence.TaskRepository;

class ReminderServiceTest {

    private StubTaskRepository repository;
    private Clock clock;

    @BeforeEach
    void setUp() {
        repository = new StubTaskRepository();
        clock = Clock.fixed(Instant.parse("2025-11-07T01:00:00Z"), ZoneOffset.UTC);
    }

    @Test
    void dispatchDueRemindersNotifiesAndMarks() {
        TaskEntity entity = new TaskEntity(1L, "Title", LocalDateTime.of(2025, 11, 7, 1, 0), "user@example.com", null);
        repository.pending.add(entity);
        CollectingNotifier notifier = new CollectingNotifier();
        ReminderService service = new ReminderService(repository, notifier, clock);

        service.dispatchDueReminders();

        assertThat(notifier.tasks).hasSize(1);
        assertThat(repository.markedIds).containsExactly(1L);
    }

    @Test
    void dispatchDueRemindersSkipsFailures() {
        TaskEntity entity = new TaskEntity(2L, "Title", LocalDateTime.of(2025, 11, 7, 1, 0), "user@example.com", null);
        repository.pending.add(entity);
        ReminderService service = new ReminderService(repository, task -> {
            throw new RuntimeException("boom");
        }, clock);

        service.dispatchDueReminders();

        assertThat(repository.markedIds).isEmpty();
    }

    private static final class StubTaskRepository extends TaskRepository {

        final List<TaskEntity> pending = new ArrayList<>();
        final List<Long> markedIds = new ArrayList<>();

        StubTaskRepository() {
            super(null);
        }

        @Override
        public List<TaskEntity> findPendingReminders(LocalDateTime threshold) {
            return new ArrayList<>(pending);
        }

        @Override
        public void markReminded(List<Long> ids, LocalDateTime when) {
            markedIds.clear();
            markedIds.addAll(ids);
        }
    }

    private static final class CollectingNotifier implements ReminderNotifier {

        private final List<Task> tasks = new ArrayList<>();

        @Override
        public void notify(Task task) {
            tasks.add(task);
        }
    }
}
