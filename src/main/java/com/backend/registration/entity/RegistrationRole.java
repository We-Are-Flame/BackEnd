package com.backend.registration.entity;

public enum RegistrationRole {
    OWNER, MEMBER;
    public boolean isOwner(RegistrationRole registrationRole) {
        return registrationRole == OWNER;
    }
}
