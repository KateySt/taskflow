package org.taskflow.com.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.taskflow.com.annotation.CheckToken;
import org.taskflow.com.entity.TaskEntity;
import org.taskflow.com.entity.UserEntity;
import org.taskflow.com.model.CreateTaskDTO;
import org.taskflow.com.model.TaskDTO;
import org.taskflow.com.model.UpdateTaskDTO;
import org.taskflow.com.repository.ProjectRepository;
import org.taskflow.com.repository.TaskRepository;
import org.taskflow.com.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TokenCacheServiceImpl tokenCacheService;

    /**
     * Creates a new task and assigns it to a user and a project.
     *
     * @param createTaskDTO - DTO containing the task details.
     * @param authHeader - Authorization token for user authentication.
     * @return TaskDTO - The created task.
     * @throws EntityNotFoundException - Thrown if the user or project is not found.
     */
    @CheckToken
    public TaskDTO createTask(CreateTaskDTO createTaskDTO, String authHeader) {
        UserEntity user = userRepository.findByEmail(tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        var project = projectRepository.findById(createTaskDTO.projectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        TaskEntity task = TaskEntity.builder()
                .title(createTaskDTO.title())
                .description(createTaskDTO.description())
                .priority(createTaskDTO.priority())
                .status(createTaskDTO.status())
                .assignedTo(user)
                .deadline(createTaskDTO.deadline())
                .project(project)
                .createdAt(LocalDateTime.now())
                .build();

        TaskEntity savedTask = taskRepository.save(task);

        return toTaskDTO(savedTask);
    }

    /**
     * Retrieves a list of tasks assigned to the user.
     *
     * @param authHeader - Authorization token for user authentication.
     * @return List<TaskDTO> - A list of task DTOs.
     */
    @CheckToken
    public List<TaskDTO> getTasks(String authHeader) {
        return taskRepository.findByAssignedTo_Email(tokenCacheService.getEmailByToken(authHeader))
                .stream()
                .map(this::toTaskDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific task by its ID if it is assigned to the authenticated user.
     *
     * @param taskId - The ID of the task.
     * @param authHeader - Authorization token for user authentication.
     * @return TaskDTO - The task with the specified ID.
     * @throws EntityNotFoundException - Thrown if the task is not found.
     */
    @CheckToken
    public TaskDTO getTaskById(Long taskId, String authHeader) {
        TaskEntity task = taskRepository.findByIdAndAssignedTo_Email(taskId, tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return toTaskDTO(task);
    }

    /**
     * Updates an existing task assigned to the authenticated user.
     *
     * @param taskId - The ID of the task to update.
     * @param updateTaskDTO - DTO containing updated task details.
     * @param authHeader - Authorization token for user authentication.
     * @return TaskDTO - The updated task.
     * @throws EntityNotFoundException - Thrown if the task is not found.
     */
    @CheckToken
    public TaskDTO updateTask(Long taskId, UpdateTaskDTO updateTaskDTO, String authHeader) {
        TaskEntity task = taskRepository.findByIdAndAssignedTo_Email(taskId, tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        task.setTitle(updateTaskDTO.title());
        task.setDescription(updateTaskDTO.description());
        task.setPriority(updateTaskDTO.priority());
        task.setStatus(updateTaskDTO.status());
        task.setDeadline(updateTaskDTO.deadline());

        TaskEntity updatedTask = taskRepository.save(task);

        return toTaskDTO(updatedTask);
    }

    /**
     * Deletes a task assigned to the authenticated user.
     *
     * @param taskId - The ID of the task to delete.
     * @param authHeader - Authorization token for user authentication.
     * @throws EntityNotFoundException - Thrown if the task is not found.
     */
    @CheckToken
    public void deleteTask(Long taskId, String authHeader) {
        TaskEntity task = taskRepository.findByIdAndAssignedTo_Email(taskId, tokenCacheService.getEmailByToken(authHeader))
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        taskRepository.delete(task);
    }

    /**
     * Converts a TaskEntity to TaskDTO.
     *
     * @param task - The TaskEntity to convert.
     * @return TaskDTO - The corresponding TaskDTO.
     */
    private TaskDTO toTaskDTO(TaskEntity task) {
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getPriority(), task.getStatus(), task.getAssignedTo().getEmail(), task.getDeadline(), task.getCreatedAt());
    }
}