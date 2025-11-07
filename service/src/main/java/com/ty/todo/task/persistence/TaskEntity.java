package com.ty.todo.task.persistence;

import java.time.LocalDateTime;

/**
 * Persistence model that MyBatis maps to the tasks table.
 */
public class TaskEntity {
    private Long id;
    private String title;
    private LocalDateTime remindAt;
    private String email;
    private LocalDateTime remindedAt;

    public TaskEntity() {
    }

    public TaskEntity(Long id, String title, LocalDateTime remindAt, String email, LocalDateTime remindedAt) {
        this.id = id;
        this.title = title;
        this.remindAt = remindAt;
        this.email = email;
        this.remindedAt = remindedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(LocalDateTime remindAt) {
        this.remindAt = remindAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRemindedAt() {
        return remindedAt;
    }

    public void setRemindedAt(LocalDateTime remindedAt) {
        this.remindedAt = remindedAt;
    }
}
