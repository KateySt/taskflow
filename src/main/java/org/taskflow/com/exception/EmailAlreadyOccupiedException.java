package org.taskflow.com.exception;

public class EmailAlreadyOccupiedException extends RuntimeException {
    public EmailAlreadyOccupiedException(String email) {
        super("Email '" + email + "' is already occupied");
    }
}