package com.SmartHealthcare.repository.doctor;

import com.SmartHealthcare.model.doctor.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository <DoctorAvailability, Long> {
    List<DoctorAvailability> findByDoctorDoctorIdAndAvailableDateBetween(Long doctorId, LocalDate start, LocalDate end);
    List<DoctorAvailability> findByDoctorDoctorId(Long doctorId);

}
