package com.SmartHealthcare.dto.response.appointment;

import com.SmartHealthcare.constants.AppointmentStatus;
import com.SmartHealthcare.constants.AppointmentType;
import com.SmartHealthcare.constants.PaymentStatus;
import com.SmartHealthcare.dto.response.doctor.DoctorAvailabilityDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {
    private Long appointmentId;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private DoctorAvailabilityDTO availability;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentType appointmentType;
    private AppointmentStatus status;
    private Double consultationFee;
    private PaymentStatus paymentStatus;
    private String symptoms;
    private String notes;
    private String cancellationReason;
    private Long cancelledBy;
    private boolean reminderSent;
}