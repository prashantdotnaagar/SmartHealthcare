package com.SmartHealthcare.dto.request.user;

import com.SmartHealthcare.constants.UserRole;
import lombok.Data;

@Data
public class RegisterRequestUser {
    private String userName;
    private String userEmail;
    private String userPassword;
    private UserRole role;
}
