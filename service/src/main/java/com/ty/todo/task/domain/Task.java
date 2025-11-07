package com.ty.todo.task.domain;

import java.time.LocalDateTime;

/**
 * Domain aggregate representing a todo task.
 */
public record Task(Long id, String title, LocalDateTime remindAt, String email, LocalDateTime remindedAt) {

    public Task {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        title = title.trim();
        if (email != null) {
            email = email.isBlank() ? null : email.trim();
        }
    }

    public Task withId(Long newId) {
        return new Task(newId, title, remindAt, email, remindedAt);
    }

    public Task reminded(LocalDateTime when) {
        return new Task(id, title, remindAt, email, when);
    }
}
