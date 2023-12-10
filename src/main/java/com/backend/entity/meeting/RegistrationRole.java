package com.backend.entity.meeting;

public enum RegistrationRole {
    OWNER, MEMBER;

    public boolean isOwner(RegistrationRole registrationRole) {
        return registrationRole == OWNER;
    }
}
