package com.ty.todo.task.reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.ty.todo.task.domain.Task;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Sends reminders via configured JavaMailSender.
 */
class MailReminderNotifier implements ReminderNotifier {

    private static final Logger log = LoggerFactory.getLogger(MailReminderNotifier.class);

    private final JavaMailSender mailSender;
    private final ReminderProperties properties;

    MailReminderNotifier(JavaMailSender mailSender, ReminderProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }

    @Override
    public void notify(Task task) {
        if (task.email() == null) {
            log.debug("Skip reminder for task {} because email is null", task.id());
            return;
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
            helper.setTo(task.email());
            helper.setFrom(properties.getMail().getFrom());
            helper.setSubject(properties.getMail().getSubject());
            helper.setText(buildBody(task), false);
            mailSender.send(mimeMessage);
            log.info("Sent reminder email for task {}", task.id());
        } catch (MessagingException ex) {
            throw new IllegalStateException("Failed to compose reminder email", ex);
        }
    }

    private String buildBody(Task task) {
        StringBuilder body = new StringBuilder("Hi,\n\nThis is a reminder for your task: \"")
                .append(task.title())
                .append("\".");
        if (task.remindAt() != null) {
            body.append(" Due at: ").append(task.remindAt());
        }
        body.append("\n\n-- todo-service");
        return body.toString();
    }
}
