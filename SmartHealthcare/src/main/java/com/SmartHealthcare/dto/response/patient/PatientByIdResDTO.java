package com.SmartHealthcare.dto.response.patient;

import com.SmartHealthcare.constants.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientByIdResDTO {
    private long userId;
    private String  firstName;
    private String  lastName;
    private String  address;
    private LocalDate dateOfBirth;
    private String emergencyPhone;
    private String phone;
    private Gender gender;
}
