package com.SmartHealthcare.dto.request.appointment;

import com.SmartHealthcare.constants.AppointmentType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientBookAppointmentDTO {
    private Long doctorId;
    private Long patientId;
    private Long availabilityId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentType appointmentType;
    private String symptoms;
}