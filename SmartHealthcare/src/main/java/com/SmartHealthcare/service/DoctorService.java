package com.SmartHealthcare.service;

import com.SmartHealthcare.dto.request.doctor.FilterRequest;
import com.SmartHealthcare.model.Doctor;
import java.util.List;

public interface DoctorService {
    void saveDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Doctor getDoctorById(Long id);
    List<Doctor> searchDoctors(String specialization, String location, String language, Integer minexperience, Integer maxexperience);
    List<Doctor>filterDoctors(FilterRequest filterRequest);
}
