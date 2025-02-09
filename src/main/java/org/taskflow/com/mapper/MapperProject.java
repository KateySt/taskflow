package org.taskflow.com.mapper;

import org.mapstruct.Mapper;
import org.taskflow.com.entity.ProjectEntity;
import org.taskflow.com.entity.UserEntity;
import org.taskflow.com.model.CreateProjectDTO;
import org.taskflow.com.model.ProjectDTO;

import java.time.LocalDateTime;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface MapperProject {

    default ProjectEntity toProjectEntity(CreateProjectDTO createProjectDTO, UserEntity user) {
        return ProjectEntity.builder()
                .name(createProjectDTO.name())
                .description(createProjectDTO.description())
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();
    }

    default ProjectDTO toProjectDTO(ProjectEntity project) {
        return new ProjectDTO(project.getId(), project.getName(), project.getDescription(), project.getCreatedAt());
    }
}
