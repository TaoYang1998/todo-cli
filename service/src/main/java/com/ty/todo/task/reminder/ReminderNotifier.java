package com.ty.todo.task.reminder;

import com.ty.todo.task.domain.Task;

/**
 * Abstraction for delivering reminder notifications.
 */
public interface ReminderNotifier {

    void notify(Task task);
}
