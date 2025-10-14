package com.SmartHealthcare.dto.request.availability;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SlotDTO {
    private LocalTime startTime;
    private LocalTime endTime;
    private String status; // FREE or BOOKED
    private Long appointmentId;
    private Long patientId;
}
