package org.taskflow.com.service;

import org.taskflow.com.model.CreateTaskDTO;
import org.taskflow.com.model.TaskDTO;
import org.taskflow.com.model.UpdateTaskDTO;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(CreateTaskDTO createTaskDTO, String authHeader);

    List<TaskDTO> getTasks(String authHeader);

    TaskDTO getTaskById(Long taskId, String authHeader);

    TaskDTO updateTask(Long taskId, UpdateTaskDTO updateTaskDTO, String authHeader);

    void deleteTask(Long taskId, String authHeader);
}
