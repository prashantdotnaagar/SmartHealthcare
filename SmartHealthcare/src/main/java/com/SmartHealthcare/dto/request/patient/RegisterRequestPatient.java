package com.SmartHealthcare.dto.request.patient;

import com.SmartHealthcare.dto.request.user.BaseUserRequest;
import com.SmartHealthcare.constants.Gender;
import com.SmartHealthcare.constants.UserRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequestPatient implements BaseUserRequest {
    private String userName;
    private String userEmail;
    private String userPassword;
    private UserRole role;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String address;
    private String emergencyPhoneNumber;
}
