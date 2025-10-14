package com.SmartHealthcare.dto.request.appointment;


import com.SmartHealthcare.constants.AppointmentStatus;
import com.SmartHealthcare.constants.AppointmentType;
import com.SmartHealthcare.constants.PaymentStatus;
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
public class DoctorScheduleAppointmentDTO {
    private Long doctorId;
    private Long patientId;
    private Long availabilityId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentType appointmentType;
    private AppointmentStatus status;
    private PaymentStatus paymentStatus;
    private Double consultationFee;
    private String symptoms;
    private String notes;
}
