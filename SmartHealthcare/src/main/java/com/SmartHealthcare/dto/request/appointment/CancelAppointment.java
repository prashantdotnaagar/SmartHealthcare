package com.SmartHealthcare.dto.request.appointment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CancelAppointment {
    private Long appointmentId;
    private String cancellationReason;
    private String cancelledBy;
    private LocalDateTime cancelledAt;
}
