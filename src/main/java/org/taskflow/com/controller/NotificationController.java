package org.taskflow.com.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @MessageMapping("/notifications")
    @SendTo("/topic/updates")
    public String sendNotification(String message) {
        return "New status update: " + message;
    }
}