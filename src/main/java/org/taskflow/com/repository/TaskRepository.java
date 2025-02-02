package org.taskflow.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.taskflow.com.entity.TaskEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Optional<TaskEntity> findByAssignedTo_Email(String emailByToken);

    Optional<TaskEntity> findByIdAndAssignedTo_Email(Long taskId, String emailByToken);

    List<TaskEntity> findByDeadlineBetween(LocalDateTime deadlineStart, LocalDateTime deadlineEnd);
}
