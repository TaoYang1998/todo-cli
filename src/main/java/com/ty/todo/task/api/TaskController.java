package com.ty.todo.task.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ty.todo.task.api.dto.TaskRequest;
import com.ty.todo.task.api.dto.TaskResponse;
import com.ty.todo.task.domain.Task;
import com.ty.todo.task.service.TaskService;

import jakarta.validation.Valid;

/**
 * REST controller exposing the task endpoints.
 */
@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        Task created = service.createTask(request.title(), request.remindAt(), request.email());
        TaskResponse body = new TaskResponse(created.id(), created.title(), created.remindAt(), created.email());
        URI location = created.id() == null ? URI.create("/api/tasks") : URI.create("/api/tasks/" + created.id());
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping
    public List<TaskResponse> list() {
        return service.listTasks().stream()
                .map(task -> new TaskResponse(task.id(), task.title(), task.remindAt(), task.email()))
                .toList();
    }
}
