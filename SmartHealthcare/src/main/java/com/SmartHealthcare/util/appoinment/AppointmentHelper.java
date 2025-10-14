package com.SmartHealthcare.util.appoinment;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppointmentHelper {
    public static List<DayOfWeek> parseDays(String dayRange) {
        Map<String, DayOfWeek> dayMap = Map.of(
                "MON", DayOfWeek.MONDAY,
                "TUE", DayOfWeek.TUESDAY,
                "WED", DayOfWeek.WEDNESDAY,
                "THU", DayOfWeek.THURSDAY,
                "FRI", DayOfWeek.FRIDAY,
                "SAT", DayOfWeek.SATURDAY,
                "SUN", DayOfWeek.SUNDAY
        );

        if (dayRange.contains("-")) {
            String[] parts = dayRange.split("-");
            DayOfWeek start = dayMap.get(parts[0].trim().toUpperCase());
            DayOfWeek end = dayMap.get(parts[1].trim().toUpperCase());
            List<DayOfWeek> days = new ArrayList<>();
            DayOfWeek current = start;
            while (true) {
                days.add(current);
                if (current == end) break;
                current = current.plus(1);
            }
            return days;
        } else {
            return List.of(dayMap.get(dayRange.trim().toUpperCase()));
        }
    }

}
