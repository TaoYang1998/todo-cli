package com.ty.todo.task.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

class TaskServiceTest {

    private RecordingRepository repository;
    private TaskService service;

    @BeforeEach
    void setUp() {
        repository = new RecordingRepository();
        Clock clock = Clock.fixed(Instant.parse("2025-11-07T00:00:00Z"), ZoneOffset.UTC);
        service = new TaskService(repository, clock);
    }

    @Test
    void createTaskRejectsPastReminder() {
        LocalDateTime past = LocalDateTime.of(2025, 11, 6, 23, 59);
        assertThatThrownBy(() -> service.createTask("test", past, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("remindAt must not be in the past");
    }

    @Test
    void createTaskTrimsInputAndPersists() {
        LocalDateTime future = LocalDateTime.of(2025, 11, 7, 8, 0);

        Task created = service.createTask("  Test Title  ", future, " user@example.com ");

        assertThat(created.id()).isEqualTo(1L);
        TaskEntity saved = repository.storage.get(0);
        assertThat(saved.getTitle()).isEqualTo("Test Title");
        assertThat(saved.getEmail()).isEqualTo("user@example.com");
    }

    @Test
    void listTasksMapsEntities() {
        repository.save(new TaskEntity(null, "Title", LocalDateTime.of(2025, 11, 7, 9, 0), "me@example.com", null));

        List<Task> tasks = service.listTasks();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).title()).isEqualTo("Title");
    }

    private static final class RecordingRepository extends TaskRepository {

        private final List<TaskEntity> storage = new ArrayList<>();
        private long nextId = 1L;

        RecordingRepository() {
            super(null);
        }

        @Override
        public TaskEntity save(TaskEntity entity) {
            if (entity.getId() == null) {
                entity.setId(nextId++);
            }
            storage.add(entity);
            return entity;
        }

        @Override
        public List<TaskEntity> findAll() {
            return new ArrayList<>(storage);
        }
    }
}
