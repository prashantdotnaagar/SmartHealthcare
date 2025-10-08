package com.SmartHealthcare.dto.request.doctor;


import lombok.Data;

@Data
public class FilterRequest {
    private String availability;
    private Double consultationFees;
    private Double ratings;
}
