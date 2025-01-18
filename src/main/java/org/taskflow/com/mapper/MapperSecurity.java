package org.taskflow.com.mapper;

import org.mapstruct.Mapper;
import org.taskflow.com.entity.UserEntity;
import org.taskflow.com.model.SessionInfo;
import org.taskflow.com.service.impl.UserDetailsImpl;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface MapperSecurity {
    default SessionInfo toSessionInfo(String token) {
        return SessionInfo.builder()
                .token(token)
                .build();
    }

    default UserDetailsImpl toUserDetailsImpl(UserEntity user) {
        return new UserDetailsImpl(
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }
}