package com.ty.todo.task.reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ty.todo.task.domain.Task;

/**
 * Fallback notifier that only logs reminder attempts.
 */
class LoggingReminderNotifier implements ReminderNotifier {

    private static final Logger log = LoggerFactory.getLogger(LoggingReminderNotifier.class);

    @Override
    public void notify(Task task) {
        log.info("Reminder (mock) for task {} / email {}", task.id(), task.email());
    }
}
