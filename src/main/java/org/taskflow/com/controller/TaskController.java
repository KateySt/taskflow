package org.taskflow.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.taskflow.com.model.CreateTaskDTO;
import org.taskflow.com.model.TaskDTO;
import org.taskflow.com.model.UpdateTaskDTO;
import org.taskflow.com.service.TaskService;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Endpoints for managing tasks")
public class TaskController {

    private final TaskService taskService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Creates a new task for the authenticated user.
     *
     * @param createTaskDTO the task creation data
     * @param authHeader the authorization header containing the JWT token
     * @return the created task DTO
     */
    @Operation(
            summary = "Create Task",
            description = "Creates a new task for the authenticated user.",
            tags = {"Tasks"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Task created successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found for the provided token."
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO createTask(@RequestBody CreateTaskDTO createTaskDTO, @RequestHeader("Authorization") String authHeader) {
        try {
            TaskDTO createdTask = taskService.createTask(createTaskDTO, authHeader);
            messagingTemplate.convertAndSend("/task-status/updates",
                    "Task created: " + createdTask.id() + " - " + createdTask.status());
            return createdTask;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    /**
     * Gets all tasks assigned to the authenticated user.
     *
     * @param authHeader the authorization header containing the JWT token
     * @return a list of task DTOs
     */
    @Operation(
            summary = "Get Tasks",
            description = "Retrieves all tasks assigned to the authenticated user.",
            tags = {"Tasks"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tasks retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)
                    )
            )
    })
    @GetMapping
    public List<TaskDTO> getTasks(@RequestHeader("Authorization") String authHeader) {
        return taskService.getTasks(authHeader);
    }

    /**
     * Retrieves a specific task by ID for the authenticated user.
     *
     * @param id the task ID
     * @param authHeader the authorization header containing the JWT token
     * @return the task DTO
     */
    @Operation(
            summary = "Get Task by ID",
            description = "Retrieves a specific task by ID assigned to the authenticated user.",
            tags = {"Tasks"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found for the given ID."
            )
    })
    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        try {
            return taskService.getTaskById(id, authHeader);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
        }
    }

    /**
     * Updates an existing task assigned to the authenticated user.
     *
     * @param id the task ID
     * @param updateTaskDTO the task update data
     * @param authHeader the authorization header containing the JWT token
     * @return the updated task DTO
     */
    @Operation(
            summary = "Update Task",
            description = "Updates an existing task for the authenticated user.",
            tags = {"Tasks"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task updated successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found for the given ID."
            )
    })
    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable Long id, @RequestBody UpdateTaskDTO updateTaskDTO, @RequestHeader("Authorization") String authHeader) {
        try {
            TaskDTO updatedTask = taskService.updateTask(id, updateTaskDTO, authHeader);
            messagingTemplate.convertAndSend("/task-status/updates",
                    "Task updated: " + updatedTask.id() + " - " + updatedTask.status());
            return updatedTask;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
        }
    }

    /**
     * Deletes a task by ID for the authenticated user.
     *
     * @param id the task ID
     * @param authHeader the authorization header containing the JWT token
     */
    @Operation(
            summary = "Delete Task",
            description = "Deletes a task by ID for the authenticated user.",
            tags = {"Tasks"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Task deleted successfully."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found for the given ID."
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        try {
            taskService.deleteTask(id, authHeader);
            messagingTemplate.convertAndSend("/task-status/updates",
                    "Task deleted: " + id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
        }
    }
}