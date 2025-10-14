package com.SmartHealthcare.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Data
public class WeekSchedule {
    private Long doctorId;
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private List<DaySchedule> schedule;
}
