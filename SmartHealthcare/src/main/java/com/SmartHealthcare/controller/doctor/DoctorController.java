package com.SmartHealthcare.controller.doctor;

import com.SmartHealthcare.constants.ServiceCodes;
import com.SmartHealthcare.dto.response.appointment.AppointmentDTO;
import com.SmartHealthcare.dto.DoctorDTO;
import com.SmartHealthcare.dto.request.doctor.FilterRequest;
import com.SmartHealthcare.dto.request.doctor.SearchRequest;
import com.SmartHealthcare.dto.response.doctor.DoctorAvailabilityDTO;
import com.SmartHealthcare.dto.schedule.WeekSchedule;
import com.SmartHealthcare.model.appointment.Appointment;
import com.SmartHealthcare.model.doctor.Doctor;
import com.SmartHealthcare.model.doctor.DoctorAvailability;
import com.SmartHealthcare.service.doctor.DoctorService;
import com.SmartHealthcare.service.user.UserService;
import com.SmartHealthcare.util.appoinment.AppointmentConverter;
import com.SmartHealthcare.util.doctor.DoctorDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<DoctorDTO> doctorDTOList = doctors.stream()
                .map(DoctorDTOConverter::convertToDoctorDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctorDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DoctorDTO>> getDoctorById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(ServiceCodes.INVALID_DOCTOR_ID.getMessage());
        }

        // Service will throw ResourceNotFoundException if not found
        List<Doctor> doctor = doctorService.getDoctorById(id);

        List<DoctorDTO> dtoList = doctor.stream()
                .map(DoctorDTOConverter::convertToDoctorDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(ServiceCodes.INVALID_DOCTOR_ID.getMessage());
        }

        userService.deleteUser(id);
        return ResponseEntity.ok(ServiceCodes.SUCCESS.getMessage());
    }

    @PostMapping("/search")
    public ResponseEntity<List<DoctorDTO>> searchDoctors(@RequestBody @Valid SearchRequest request) {
        List<Doctor> result = doctorService.searchDoctors(
                request.getSpecialization(),
                request.getLocation(),
                request.getLanguage(),
                request.getMinExperience(),
                request.getMaxExperience()
        );

        List<DoctorDTO> doctorDTOList = result.stream()
                .map(DoctorDTOConverter::convertToDoctorDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctorDTOList);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Doctor>> filterDoctors(@RequestBody @Valid FilterRequest filterRequest) {
        List<Doctor> doctors = doctorService.filterDoctors(filterRequest);
        return ResponseEntity.ok(doctors);
    }
    //Exact amount of consultaion fees , change it to round amount or nearest amount in data

    @GetMapping("/{doctorId}/schedule")
    public ResponseEntity<WeekSchedule> getDoctorWeekSchedule(
            @PathVariable Long doctorId,
            @RequestParam String weekStart
    ) {
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException(ServiceCodes.INVALID_DOCTOR_ID.getMessage());
        }

        try {
            LocalDate startDate = LocalDate.parse(weekStart, DateTimeFormatter.ISO_DATE);
            WeekSchedule schedule = doctorService.getDoctorWeekSchedule(doctorId, startDate);
            return ResponseEntity.ok(schedule);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
        }
    }

    @PostMapping("/availability")
    public ResponseEntity<DoctorAvailability> createAvailability(
            @RequestBody @Valid DoctorAvailability availability
    ) {
        DoctorAvailability saved = doctorService.saveAvailability(availability);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<List<DoctorAvailabilityDTO>> getAvailability(@PathVariable Long doctorId) {
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException(ServiceCodes.INVALID_DOCTOR_ID.getMessage());
        }

        List<DoctorAvailabilityDTO> availabilities = doctorService.getAvailabilityByDoctor(doctorId);
        return ResponseEntity.ok(availabilities);
    }

    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<List<AppointmentDTO>> getAppointments(@PathVariable Long doctorId) {
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException(ServiceCodes.INVALID_DOCTOR_ID.getMessage());
        }

        List<Appointment> appointments = doctorService.getAppointmentsByDoctor(doctorId);
        List<AppointmentDTO> appointmentDTOList = appointments.stream()
                .map(AppointmentConverter::convertToAppointmentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOList);
    }


}