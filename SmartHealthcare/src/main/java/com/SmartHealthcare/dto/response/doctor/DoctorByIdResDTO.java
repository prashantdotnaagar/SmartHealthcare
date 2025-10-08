package com.SmartHealthcare.dto.response.doctor;

import lombok.Data;

@Data
public class DoctorByIdResDTO {
    private long user_id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String phone;
    private String specialization;

}
