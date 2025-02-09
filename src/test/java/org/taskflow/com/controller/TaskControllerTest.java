package org.taskflow.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.taskflow.com.model.CreateTaskDTO;
import org.taskflow.com.model.TaskDTO;
import org.taskflow.com.model.UpdateTaskDTO;
import org.taskflow.com.service.MailService;
import org.taskflow.com.service.TaskService;
import org.taskflow.com.service.TokenCacheService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @MockBean
    private MailService mailService;

    @MockBean
    private TokenCacheService tokenCacheService;

    private String authHeader;

    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        authHeader = "Bearer testToken";

        TaskDTO taskDTO = new TaskDTO(
                1L,
                "Finish the report",
                "Complete the final report for the project by end of this week.",
                "High",
                "IN_PROGRESS",
                "user@example.com",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        TaskDTO taskDTOUpdated = new TaskDTO(
                1L,
                "Update website layout",
                "This task involves updating the homepage layout and making it more user-friendly.",
                "High",
                "IN_PROGRESS",
                "user@example.com",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(taskService.createTask(any(), eq(authHeader))).thenReturn(taskDTO);
        when(taskService.getTasks(eq(authHeader))).thenReturn(List.of(taskDTO));
        when(taskService.getTaskById(eq(1L), eq(authHeader))).thenReturn(taskDTO);
        when(taskService.updateTask(eq(1L), any(), eq(authHeader))).thenReturn(taskDTOUpdated);
    }

    @Test
    void createTask_shouldCreateTask() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(
                "Task 1",
                "This is the description of the task.",
                "High",
                "Pending",
                1L,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.title").value("Finish the report"));
    }

    @Test
    void getTasks_shouldReturnTasks() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$[0].title").value("Finish the report"));
    }

    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{id}", 1L)
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.title").value("Finish the report"));
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(
                "Update website layout",
                "This task involves updating the homepage layout and making it more user-friendly.",
                "HIGH",
                "IN_PROGRESS",
                LocalDateTime.now()
        );

        mockMvc.perform(put("/api/v1/tasks/{id}", 1L)
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.title").value("Update website layout"));
    }

    @Test
    void deleteTask_shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{id}", 1L)
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}

