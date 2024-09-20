package com.backend.before.service.auth;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    ROLE_HOST, ROLE_GUEST, ROLE_USER;

    public static Authority determineAuthority(boolean isOwner) {
        return isOwner ? ROLE_HOST : ROLE_GUEST;
    }

    @Override
    public String getAuthority() {
        return this.name();
    }
}

