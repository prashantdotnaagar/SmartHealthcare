package com.SmartHealthcare.util.doctor;

import com.SmartHealthcare.model.doctor.Doctor;
import com.SmartHealthcare.model.doctor.DoctorAvailability;
import com.SmartHealthcare.util.appoinment.AppointmentHelper;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.SmartHealthcare.repository.doctor.DoctorAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.*;
import java.util.*;

@Component
public class DoctorHelper {

    @Autowired
    private  DoctorAvailabilityRepository doctorAvailabilityRepository;

    public void populateDoctorAvailability(Doctor doctor, String availabilityStr, String availabilityStarting, String availabilityEnd) {
        String[] parts = availabilityStr.split(",");
        if (parts.length != 2) return;

        String dayPart = parts[0].trim();
        String timePart = parts[1].trim();

        String[] times = timePart.split("-");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("ha", Locale.ENGLISH);
        LocalTime startTime = LocalTime.parse(times[0].trim().toUpperCase(), timeFormatter);
        LocalTime endTime = LocalTime.parse(times[1].trim().toUpperCase(), timeFormatter);

        List<DayOfWeek> days = AppointmentHelper.parseDays(dayPart);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse(availabilityStarting, dateFormatter);
        LocalDate endDate = LocalDate.parse(availabilityEnd, dateFormatter);

        List<DoctorAvailability> availabilities = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (days.contains(date.getDayOfWeek())) {
                LocalTime slotStart = startTime;
                while (slotStart.plusHours(1).compareTo(endTime) <= 0) {
                    DoctorAvailability slot = DoctorAvailability.builder()
                            .doctor(doctor)
                            .availableDate(date)
                            .startTime(slotStart)
                            .endTime(slotStart.plusHours(1))
                            .isBooked(false)
                            .build();
                    availabilities.add(slot);
                    slotStart = slotStart.plusHours(1);
                }
            }
        }

        doctorAvailabilityRepository.saveAll(availabilities);
    }


}
