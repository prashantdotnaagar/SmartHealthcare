package com.SmartHealthcare.model;


public enum UserRole {
    ADMIN("Admin"),
    DOCTOR("Doctor"),
    PATIENT("Patient");

    private final String displayName;
    UserRole(String displayName){
        this.displayName=displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
