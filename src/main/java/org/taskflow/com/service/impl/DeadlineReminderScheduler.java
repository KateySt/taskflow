package org.taskflow.com.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.taskflow.com.entity.TaskEntity;
import org.taskflow.com.exception.DeadlineReminderException;
import org.taskflow.com.repository.TaskRepository;
import org.taskflow.com.service.MailService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class DeadlineReminderScheduler {

    private final TaskRepository taskRepository;
    private final MailService mailService;

    @Transactional
    @Scheduled(cron = "0 0 * * * ?")
    public void sendDeadlineReminder() {
        List<TaskEntity> tasks = getTasksThatAreDueInOneHour();

        tasks.forEach(this::sendReminderEmailAsync);
    }

    private void sendReminderEmailAsync(TaskEntity task) {
        CompletableFuture.runAsync(() -> {
            try {
                sendReminderEmail(task);
            } catch (Exception e) {
                throw new DeadlineReminderException(task.getId(), task.getAssignedTo().getEmail(), e);
            }
        });
    }

    private void sendReminderEmail(TaskEntity task) throws MessagingException {
        String to = task.getAssignedTo().getEmail();

        Map<String, Object> variables = Map.of(
                "taskTitle", task.getTitle(),
                "taskLink", "https://yourapp.com/tasks/" + task.getId(),
                "email", to
        );

        mailService.sendEmail(to, "Deadline Reminder: Task Due Soon!", "email-template-deadline", variables);
    }

    private List<TaskEntity> getTasksThatAreDueInOneHour() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);

        return taskRepository.findByDeadlineBetween(now, oneHourLater);
    }
}