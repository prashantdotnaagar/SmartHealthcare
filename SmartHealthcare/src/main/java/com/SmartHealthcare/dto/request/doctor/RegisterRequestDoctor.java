package com.SmartHealthcare.dto.request.doctor;

import com.SmartHealthcare.dto.request.user.BaseUserRequest;
import com.SmartHealthcare.model.UserRole;
import lombok.Data;

@Data
public class RegisterRequestDoctor implements BaseUserRequest {
    private String userName;
    private String userEmail;
    private String userPassword;
    private UserRole role;

    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String phone;
    private String specialization;
    private String qualifications;
    private String location;
    private String language;
    private Integer experience;
    private String availability;
    private Double consultationFees;
    private Double ratings;
}

