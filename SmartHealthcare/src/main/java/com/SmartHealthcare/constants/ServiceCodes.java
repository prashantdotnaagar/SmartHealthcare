package com.SmartHealthcare.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum ServiceCodes {

    // ===== Generic =====
    SUCCESS("NHC001", "Your request has been processed successfully"),
    VALIDATION_ERROR("NHC002", "Invalid request - validation failed"),
    UNAUTHORIZED("NHC003", "Unauthorized access"),
    INTERNAL_ERROR("NHC004", "Unexpected error, please try again later"),
    SAVE_FAILED("NHC005", "Failed to save the resource"),
    UPDATE_FAILED("NHC006", "Failed to update the resource"),
    DELETE_FAILED("NHC007", "Failed to delete the resource"),
    RESOURCE_CONFLICT("NHC008", "Resource already exists"),
    BUSINESS_RULE_VIOLATION("NHC009", "Business logic validation failed"),


    // ===== Patient =====
    PATIENT_NOT_FOUND("NHC101", "Patient not found"),
    INVALID_PATIENT_ID("NHC102", "Invalid patient ID"),
    DUPLICATE_PATIENT("NHC103", "Patient already exists"),
    PATIENT_SAVE_FAILED("NHC104", "Failed to save patient details"),
    PATIENT_UPDATE_FAILED("NHC105", "Failed to update patient details"),


    // ===== Doctor =====
    DOCTOR_NOT_FOUND("NHC201", "Doctor not found"),
    INVALID_DOCTOR_ID("NHC202", "Invalid doctor ID"),
    DUPLICATE_DOCTOR("NHC203", "Doctor already exists"),
    DOCTOR_SAVE_FAILED("NHC204", "Failed to save doctor details"),
    DOCTOR_UPDATE_FAILED("NHC205", "Failed to update doctor details"),
    DOCTOR_AVAILABILITY_NOT_FOUND("NHC206", "Doctor availability not found"),


    // ===== Appointment =====
    APPOINTMENT_NOT_FOUND("NHC301", "Appointment not found"),
    INVALID_APPOINTMENT_DATE("NHC302", "Invalid appointment date"),
    DOCTOR_NOT_AVAILABLE("NHC303", "Doctor not available for the selected slot"),
    DUPLICATE_APPOINTMENT("NHC304", "Appointment already exists for the same slot"),
    APPOINTMENT_SAVE_FAILED("NHC305", "Failed to save appointment"),
    APPOINTMENT_CANCEL_FAILED("NHC306", "Failed to cancel appointment"),
    APPOINTMENT_CANCELLED("NHC307","Appointment Cancelled"),
    APPOINTMENT_ALREADY_CANCELLED("NHC308","Appointment already cancelled"),



    // ===== Admin =====
    ADMIN_NOT_FOUND("NHC401", "Admin not found"),
    INVALID_ADMIN_ID("NHC402", "Invalid Admin ID"),
    DUPLICATE_ADMIN("NHC403", "Admin already exists"),


    // ===== Availability / Schedule =====
    AVAILABILITY_SAVE_FAILED("NHC501", "Failed to save doctor availability"),
    AVAILABILITY_UPDATE_FAILED("NHC502", "Failed to update doctor availability"),
    SCHEDULE_GENERATION_FAILED("NHC503", "Failed to generate doctor schedule"),
    AVAILABILITY_NOT_FOUND("NHC504","Selected availability not found"),
    SLOT_NOT_AVAILABLE("NHC505","Current slot is not available"),

    // ===== Misc / System =====
    DATABASE_ERROR("NHC601", "Database operation failed"),
    EXTERNAL_API_ERROR("NHC602", "Error communicating with external service"),
    DATA_INCONSISTENCY("NHC603", "Data inconsistency detected"),
    NOT_FOUND("NHC701","Resource Not Found");
    private final String code;
    private final String message;

    // Map for fast lookup
    private static final Map<String, ServiceCodes> LoopUp = new HashMap<>();

    static {
        for (ServiceCodes sc : ServiceCodes.values()) {
            LoopUp.put(sc.code, sc);
        }
    }

    public static ServiceCodes fromCode(String code) {
        return LoopUp.getOrDefault(code, VALIDATION_ERROR);
    }
}
