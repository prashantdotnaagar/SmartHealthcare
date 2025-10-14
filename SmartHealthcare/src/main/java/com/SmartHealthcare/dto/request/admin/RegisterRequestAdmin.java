package com.SmartHealthcare.dto.request.admin;


import com.SmartHealthcare.dto.request.user.BaseUserRequest;
import com.SmartHealthcare.constants.UserRole;
import lombok.Data;

@Data
public class RegisterRequestAdmin implements BaseUserRequest {
    private String userName;
    private String userEmail;
    private String userPassword;
    private UserRole role;

    private String firstName;
    private String lastName;
    private String department;
    private Integer accessLevel;
}
