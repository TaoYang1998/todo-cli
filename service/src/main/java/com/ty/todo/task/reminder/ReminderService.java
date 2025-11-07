package com.ty.todo.task.reminder;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ty.todo.task.domain.Task;
import com.ty.todo.task.persistence.TaskEntity;
import com.ty.todo.task.persistence.TaskRepository;

/**
 * Periodically scans for due tasks and triggers reminder delivery.
 */
@Service
public class ReminderService {

    private static final Logger log = LoggerFactory.getLogger(ReminderService.class);

    private final TaskRepository repository;
    private final ReminderNotifier notifier;
    private final Clock clock;

    public ReminderService(TaskRepository repository, ReminderNotifier notifier, Clock clock) {
        this.repository = repository;
        this.notifier = notifier;
        this.clock = clock;
    }

    @Scheduled(fixedDelayString = "${todo.reminder.poll-interval:PT30S}")
    @Transactional
    public void dispatchDueReminders() {
        LocalDateTime now = LocalDateTime.now(clock);
        List<TaskEntity> dueEntities = repository.findPendingReminders(now);
        if (dueEntities.isEmpty()) {
            return;
        }
        List<Task> dueTasks = dueEntities.stream()
                .map(entity -> new Task(entity.getId(), entity.getTitle(), entity.getRemindAt(), entity.getEmail(), entity.getRemindedAt()))
                .collect(Collectors.toList());

        log.debug("Processing {} due tasks for reminders", dueTasks.size());

        List<Long> successIds = dueTasks.stream()
                .filter(task -> task.email() != null)
                .filter(task -> {
                    try {
                        notifier.notify(task);
                        return true;
                    } catch (RuntimeException ex) {
                        log.warn("Failed to notify task {}", task.id(), ex);
                        return false;
                    }
                })
                .map(Task::id)
                .toList();

        repository.markReminded(successIds, now);
    }
}
