package com.ty.todo.task.reminder;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration knobs for reminder scheduling and notification.
 */
@ConfigurationProperties(prefix = "todo.reminder")
public class ReminderProperties {

    private Duration pollInterval = Duration.ofSeconds(30);
    private final Mail mail = new Mail();

    public Duration getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(Duration pollInterval) {
        this.pollInterval = pollInterval;
    }

    public Mail getMail() {
        return mail;
    }

    public static class Mail {
        private boolean enabled = false;
        private String from = "todo@example.com";
        private String subject = "Todo Reminder";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }
}
