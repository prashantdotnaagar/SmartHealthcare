package com.SmartHealthcare.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum ServiceCodes {
    //Generic
    SUCCESS("NHC001","Your request has been processed successfully"),
    VALIDATION_ERROR("NHC002", "Invalid request - validation failed"),
    UNAUTHORIZED("NHC003", "Unauthorized access"),
    INTERNAL_ERROR("HC004", "Unexpected error, please try again later"),

    // Patient
    PATIENT_NOT_FOUND("NHC101", "Patient not found"),
    INVALID_PATIENT_ID("NHC102", "Invalid patient ID"),
    DUPLICATE_PATIENT("NHC103", "Patient already exists"),

    // Doctor
    DOCTOR_NOT_FOUND("NHC201", "Doctor not found"),
    INVALID_DOCTOR_ID("NHC202", "Invalid doctor ID"),

    // Appointment
    APPOINTMENT_NOT_FOUND("NHC301", "Appointment not found"),
    INVALID_APPOINTMENT_DATE("NHC302", "Invalid appointment date"),
    DOCTOR_NOT_AVAILABLE("NHC303", "Doctor not available for the selected slot"),

    //Admin
    ADMIN_NOT_FOUND("NHC401", "Admin not found"),
    INVALID_ADMIN_ID("NHC402", "Invalid Admin ID");

    private final String code;
    private final String message;
    //Map to look for Codes instead of looping everytime
    private static final Map<String, ServiceCodes>LoopUp=new HashMap<>();

    //Allocating codes with message
    static {
        for(ServiceCodes sc : ServiceCodes.values()){
            LoopUp.put(sc.code, sc);
        }
    }

    //return Default validation error or get code by Error
    public static ServiceCodes fromCode(String code){
        return LoopUp.getOrDefault(code,VALIDATION_ERROR);
    }

}
