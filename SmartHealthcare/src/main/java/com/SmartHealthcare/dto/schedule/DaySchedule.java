package com.SmartHealthcare.dto.schedule;

import com.SmartHealthcare.dto.request.availability.SlotDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Data
public class DaySchedule {
    private LocalDate date;
    private List<SlotDTO> slots;
}

