package org.taskflow.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.taskflow.com.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByCreatedBy_Email(String email);

    Optional<ProjectEntity> findByIdAndCreatedBy_Email(Long id, String email);
}