package com.SmartHealthcare.dto.request.user;

import com.SmartHealthcare.model.UserRole;

public interface BaseUserRequest {
    String getUserName();
    String getUserEmail();
    String getUserPassword();
    UserRole getRole();
}
