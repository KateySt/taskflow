package org.taskflow.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.taskflow.com.entity.TaskEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Optional<TaskEntity> findByAssignedTo_Email(String emailByToken);

    Optional<TaskEntity> findByIdAndAssignedTo_Email(Long taskId, String emailByToken);

    List<TaskEntity> findByDeadlineBetween(LocalDateTime deadlineStart, LocalDateTime deadlineEnd);

    @Query("SELECT t.status, COUNT(t) FROM TaskEntity t WHERE t.project.id = :projectId GROUP BY t.status")
    List<Object[]> countTasksByStatusForProject(@Param("projectId") Long projectId);

    @Query("SELECT t.status, COUNT(t) FROM TaskEntity t WHERE t.assignedTo.id = :userId GROUP BY t.status")
    List<Object[]> countTasksByStatusForUser(@Param("userId") Long userId);

    @Query(value = "SELECT AVG(EXTRACT(DAY FROM (deadline - created_at))) FROM tasks WHERE project_id = :projectId", nativeQuery = true)
    Double averageCompletionTimeForProject(@Param("projectId") Long projectId);

    @Query(value = "SELECT AVG(EXTRACT(DAY FROM (deadline - created_at))) FROM tasks WHERE assigned_to = :userId", nativeQuery = true)
    Double averageCompletionTimeForUser(@Param("userId") Long userId);
}
