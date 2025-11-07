package com.ty.todo.cli.client;

/**
 * Factory abstraction mainly for tests.
 */
public interface TodoServiceClientFactory {

    TodoServiceClient create(String baseUrl);
}
