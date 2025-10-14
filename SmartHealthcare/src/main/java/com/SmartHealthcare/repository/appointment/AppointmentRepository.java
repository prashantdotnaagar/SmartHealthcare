package com.SmartHealthcare.repository.appointment;

import com.SmartHealthcare.model.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByDoctorDoctorIdAndAppointmentDateBetween(
            Long doctorId, LocalDate startDate, LocalDate endDate
    );
    List<Appointment> findByDoctorDoctorId(Long doctorId);
    List<Appointment> findByPatientPatientId(Long patientId);
}
