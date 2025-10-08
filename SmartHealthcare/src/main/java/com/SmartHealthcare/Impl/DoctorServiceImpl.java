package com.SmartHealthcare.Impl;

import com.SmartHealthcare.dto.request.doctor.FilterRequest;
import com.SmartHealthcare.model.Doctor;
import com.SmartHealthcare.repository.DoctorRepository;
import com.SmartHealthcare.service.DoctorService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public List<Doctor> searchDoctors(String specialization, String location, String language, Integer minexperience, Integer maxexperience) {
        return doctorRepository.searchDoctors(
                specialization, location, language, minexperience, maxexperience
        );
    }

    @Override
    public void saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + id));
    }

    public List<Doctor> filterDoctors(FilterRequest filterRequest) {
        return doctorRepository.filterDoctors(
                filterRequest.getAvailability(),
                filterRequest.getConsultationFees(),
                filterRequest.getRatings()
        );
    }
}
