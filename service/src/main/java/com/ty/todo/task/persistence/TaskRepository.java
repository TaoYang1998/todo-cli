package com.ty.todo.task.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * Repository bridge between the domain and MyBatis mapper.
 */
@Repository
public class TaskRepository {

    private final TaskMapper mapper;

    public TaskRepository(TaskMapper mapper) {
        this.mapper = mapper;
    }

    public TaskEntity save(TaskEntity entity) {
        mapper.insert(entity);
        return entity;
    }

    public List<TaskEntity> findAll() {
        return mapper.findAll();
    }

    public List<TaskEntity> findPendingReminders(LocalDateTime threshold) {
        return mapper.findPendingReminders(threshold);
    }

    public void markReminded(List<Long> ids, LocalDateTime when) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        mapper.markReminded(ids, when);
    }
}
