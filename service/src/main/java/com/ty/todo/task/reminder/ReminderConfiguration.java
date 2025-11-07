package com.ty.todo.task.reminder;

import java.time.Clock;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Infrastructure beans for reminder scheduling.
 */
@Configuration
@EnableConfigurationProperties(ReminderProperties.class)
public class ReminderConfiguration {

    @Bean
    Clock systemClock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    ReminderNotifier reminderNotifier(ReminderProperties properties, ObjectProvider<JavaMailSender> senderProvider) {
        JavaMailSender sender = senderProvider.getIfAvailable();
        if (properties.getMail().isEnabled() && sender != null) {
            return new MailReminderNotifier(sender, properties);
        }
        return new LoggingReminderNotifier();
    }
}
