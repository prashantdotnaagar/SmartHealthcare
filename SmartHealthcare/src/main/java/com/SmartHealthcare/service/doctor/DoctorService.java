package com.SmartHealthcare.service.doctor;

import com.SmartHealthcare.dto.request.doctor.FilterRequest;
import com.SmartHealthcare.dto.response.doctor.DoctorAvailabilityDTO;
import com.SmartHealthcare.dto.schedule.WeekSchedule;
import com.SmartHealthcare.model.appointment.Appointment;
import com.SmartHealthcare.model.doctor.Doctor;
import com.SmartHealthcare.model.doctor.DoctorAvailability;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {
    void saveDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    List<Doctor> getDoctorById(Long id);
    List<Doctor> searchDoctors(String specialization, String location, String language, Integer minExperience, Integer maxExperience);
    List<Doctor> filterDoctors(FilterRequest filterRequest);
    WeekSchedule getDoctorWeekSchedule(Long doctorId, LocalDate weekStart);
    DoctorAvailability saveAvailability(DoctorAvailability availability);
    List<DoctorAvailabilityDTO> getAvailabilityByDoctor(Long doctorId);


    List<Appointment> getAppointmentsByDoctor(Long doctorId);
}
