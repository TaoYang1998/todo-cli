package com.ty.todo.task.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ty.todo.task.domain.Task;
import com.ty.todo.task.persistence.TaskEntity;
import com.ty.todo.task.persistence.TaskRepository;

/**
 * Application service encapsulating task use-cases.
 */
@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task createTask(String rawTitle, LocalDateTime remindAt, String rawEmail) {
        if (remindAt != null && remindAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("remindAt must not be in the past");
        }

        Task normalized = new Task(null, rawTitle, remindAt, rawEmail);
        TaskEntity entity = new TaskEntity(null, normalized.title(), normalized.remindAt(), normalized.email());
        repository.save(entity);
        return normalized.withId(entity.getId());
    }

    public List<Task> listTasks() {
        return repository.findAll().stream()
                .map(entity -> new Task(entity.getId(), entity.getTitle(), entity.getRemindAt(), entity.getEmail()))
                .toList();
    }
}
