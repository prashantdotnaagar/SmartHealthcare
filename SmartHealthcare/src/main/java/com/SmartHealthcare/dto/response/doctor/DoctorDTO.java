package com.SmartHealthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {
    private Long doctorId;
    private String firstName;
    private String lastName;
    private String fullName; // derived
    private String specialization;
    private String licenseNumber;
    private String phone;
    private String qualifications;
    private String location;
    private String language;
    private Integer experience;
    private String availability;
    private Double consultationFees;
    private BigDecimal ratings;
    private String reviews;
    private List<Long> appointmentIds;
}
