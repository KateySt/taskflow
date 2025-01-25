package org.taskflow.com.service;

import org.springframework.security.core.Authentication;
import org.taskflow.com.model.NewUser;
import org.taskflow.com.model.SessionInfo;
import org.taskflow.com.service.impl.UserDetailsImpl;

import java.util.Objects;

public interface SecurityService {
    SessionInfo register(NewUser newUser);

    SessionInfo loginInfo(Authentication auth);

    default boolean checkingLoggedAndToken(long userId, Authentication auth) {
        return Objects.equals(auth.getName(), String.valueOf(userId));
    }

    String getJWTToken(UserDetailsImpl authentication, long id);

    String createScope(UserDetailsImpl authentication);
}