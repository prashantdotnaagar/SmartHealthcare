package com.SmartHealthcare.dto;

import com.SmartHealthcare.model.Gender;
import com.SmartHealthcare.model.UserRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegiesterRequest {

    private String userName;
    private String userPassword;
    private String userEmail;
    private UserRole role ;

    private Integer accessLevel;
    private String department;

    private String firstName;
    private String lastName;
    private String phoneNumber;


    private String licenseNumber;
    private String specialization;

    private String address;
    private String emergencyPhoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;

}
