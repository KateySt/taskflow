package org.taskflow.com.exception;

public class DeadlineReminderException extends RuntimeException {
    public DeadlineReminderException(Long taskId, String email, Throwable cause) {
        super("Error sending email for task " + taskId + " : " + email, cause);
    }
}
