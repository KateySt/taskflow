package org.taskflow.com.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.taskflow.com.entity.ProjectEntity;
import org.taskflow.com.entity.TaskEntity;
import org.taskflow.com.entity.UserEntity;
import org.taskflow.com.model.CreateTaskDTO;
import org.taskflow.com.model.TaskDTO;
import org.taskflow.com.model.UpdateTaskDTO;
import org.taskflow.com.repository.ProjectRepository;
import org.taskflow.com.repository.TaskRepository;
import org.taskflow.com.repository.UserRepository;
import org.taskflow.com.service.TokenCacheService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TokenCacheService tokenCacheService;

    @InjectMocks
    private TaskServiceImpl taskService;

    private final String authHeader = "Bearer token";
    private final String email = "user@example.com";
    private final Long taskId = 1L;
    private final Long projectId = 1L;
    private UserEntity user;
    private ProjectEntity project;
    private TaskEntity task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new UserEntity();
        user.setEmail(email);

        project = new ProjectEntity();
        project.setId(projectId);

        task = TaskEntity.builder()
                .id(taskId)
                .title("Test Task")
                .description("Test Description")
                .priority("High")
                .status("Open")
                .assignedTo(user)
                .project(project)
                .deadline(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createTask_ShouldCreateTask() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO("Test Task", "Test Description", "High", "Open", projectId, LocalDateTime.now().plusDays(1));
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);

        TaskDTO createdTask = taskService.createTask(createTaskDTO, authHeader);

        assertNotNull(createdTask);
        assertEquals(taskId, createdTask.id());
        assertEquals("Test Task", createdTask.title());
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    void createTask_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO("Test Task", "Test Description", "High", "Open", projectId, LocalDateTime.now().plusDays(1));
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.createTask(createTaskDTO, authHeader));
    }

    @Test
    void createTask_ShouldThrowEntityNotFoundException_WhenProjectNotFound() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO("Test Task", "Test Description", "High", "Open", projectId, LocalDateTime.now().plusDays(1));
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.createTask(createTaskDTO, authHeader));
    }

    @Test
    void getTasks_ShouldReturnListOfTasks() {
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn(email);
        when(taskRepository.findByAssignedTo_Email(email))
                .thenReturn(Optional.ofNullable(task));

        List<TaskDTO> tasks = taskService.getTasks(authHeader);

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(taskId, tasks.get(0).id());
    }

    @Test
    void getTaskById_ShouldReturnTask() {
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn(email);
        when(taskRepository.findByIdAndAssignedTo_Email(taskId, email)).thenReturn(Optional.of(task));

        TaskDTO foundTask = taskService.getTaskById(taskId, authHeader);

        assertNotNull(foundTask);
        assertEquals(taskId, foundTask.id());
        assertEquals("Test Task", foundTask.title());
    }

    @Test
    void getTaskById_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn(email);
        when(taskRepository.findByIdAndAssignedTo_Email(taskId, email)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getTaskById(taskId, authHeader));
    }

    @Test
    void updateTask_ShouldUpdateTask() {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO("Updated Task", "Updated Description", "Medium", "In Progress", LocalDateTime.now().plusDays(2));
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn(email);
        when(taskRepository.findByIdAndAssignedTo_Email(taskId, email)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);

        TaskDTO updatedTask = taskService.updateTask(taskId, updateTaskDTO, authHeader);

        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.title());
        assertEquals("Updated Description", updatedTask.description());
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    void updateTask_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO("Updated Task", "Updated Description", "Medium", "In Progress", LocalDateTime.now().plusDays(2));
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn(email);
        when(taskRepository.findByIdAndAssignedTo_Email(taskId, email)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.updateTask(taskId, updateTaskDTO, authHeader));
    }

    @Test
    void deleteTask_ShouldDeleteTask() {
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn(email);
        when(taskRepository.findByIdAndAssignedTo_Email(taskId, email)).thenReturn(Optional.of(task));

        taskService.deleteTask(taskId, authHeader);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void deleteTask_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        when(tokenCacheService.getEmailByToken(authHeader)).thenReturn(email);
        when(taskRepository.findByIdAndAssignedTo_Email(taskId, email)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask(taskId, authHeader));
    }
}
