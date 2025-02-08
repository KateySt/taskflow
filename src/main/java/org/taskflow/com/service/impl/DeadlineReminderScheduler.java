package org.taskflow.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.taskflow.com.entity.TaskEntity;
import org.taskflow.com.repository.TaskRepository;
import org.taskflow.com.service.MailService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DeadlineReminderScheduler {

    private final TaskRepository taskRepository;
    private final MailService mailService;

    @Transactional
    @Scheduled(cron = "0 0 * * * ?")
    public void sendDeadlineReminder() {
        List<TaskEntity> tasks = getTasksThatAreDueInOneHour();

        for (TaskEntity task : tasks) {
            try {
                String to = task.getAssignedTo().getEmail();

                Map<String, Object> variables = new HashMap<>();
                variables.put("taskTitle", task.getTitle());
                variables.put("taskLink", "https://yourapp.com/tasks/" + task.getId());
                variables.put("email", to);

                mailService.sendEmail(to, "Deadline Reminder: Task Due Soon!", "email-template-deadline", variables);
            } catch (Exception e) {
                System.out.println("Error sending email for task " + task.getId() + ": " + e.getMessage());
            }
        }
    }

    private List<TaskEntity> getTasksThatAreDueInOneHour() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);

        return taskRepository.findByDeadlineBetween(now, oneHourLater);
    }
}