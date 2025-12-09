package com.SmartHealthcare.util.appoinment;

import com.SmartHealthcare.model.appointment.Appointment;
import com.SmartHealthcare.model.doctor.Doctor;
import com.SmartHealthcare.repository.appointment.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CancellationHelper {



        @Autowired
        private AppointmentRepository appointmentRepository;

        public Doctor findDoctorByAppointmentId(Long appointmentId) {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));
            return appointment.getDoctor();
        }
    }
