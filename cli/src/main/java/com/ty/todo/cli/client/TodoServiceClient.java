package com.ty.todo.cli.client;

import java.util.List;

import com.ty.todo.api.dto.TaskRequest;
import com.ty.todo.api.dto.TaskResponse;

/**
 * Client abstraction for talking to the todo-service.
 */
public interface TodoServiceClient {

    TaskResponse createTask(TaskRequest request);

    List<TaskResponse> listTasks();
}
