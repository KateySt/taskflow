package org.taskflow.com.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.taskflow.com.exception.DeadlineReminderException;
import org.taskflow.com.exception.EmailAlreadyOccupiedException;

@RestControllerAdvice
public class AdviceController {
    @ExceptionHandler({
            DeadlineReminderException.class,
            EmailAlreadyOccupiedException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO badRequest(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    record ErrorDTO(String message) {
    }
}
