package org.taskflow.com.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    MANAGER,
    USER;

    final String roleName = "ROLE_" + name();

    @Override
    public String getAuthority() {
        return roleName;
    }
}