package com.SmartHealthcare.util.appoinment;

import com.SmartHealthcare.dto.request.appointment.DoctorScheduleAppointmentDTO;
import com.SmartHealthcare.dto.request.appointment.PatientBookAppointmentDTO;
import com.SmartHealthcare.dto.response.appointment.AppointmentDTO;
import com.SmartHealthcare.model.appointment.Appointment;

public class AppointmentConverter {


    public static AppointmentDTO convertToAppointmentDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        return AppointmentDTO.builder()
                .appointmentId(appointment.getAppointmentId())
                .patientId(appointment.getPatient() != null ? appointment.getPatient().getPatientId() : null)
                .patientName(appointment.getPatient() != null ? appointment.getPatient().getFullName() : null)
                .doctorId(appointment.getDoctor() != null ? appointment.getDoctor().getDoctorId() : null)
                .doctorName(appointment.getDoctor() != null ? appointment.getDoctor().getFullName() : null)
                .appointmentDate(appointment.getAppointmentDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .appointmentType(appointment.getAppointmentType())
                .status(appointment.getStatus())
                .consultationFee(appointment.getConsultationFee())
                .paymentStatus(appointment.getPaymentStatus())
                .symptoms(appointment.getSymptoms())
                .notes(appointment.getNotes())
                .cancellationReason(appointment.getCancellationReason())
                .cancelledBy(appointment.getCancelledBy())
                .reminderSent(appointment.isReminderSent())
                .build();
    }

    public static DoctorScheduleAppointmentDTO mapToDoctorScheduleDTO(Appointment appointment) {
        return DoctorScheduleAppointmentDTO.builder()
                .doctorId(appointment.getDoctor().getDoctorId())
                .patientId(appointment.getPatient().getPatientId())
                .availabilityId(appointment.getAvailability().getAvailabilityId())
                .appointmentDate(appointment.getAppointmentDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .appointmentType(appointment.getAppointmentType())
                .status(appointment.getStatus())
                .paymentStatus(appointment.getPaymentStatus())
                .consultationFee(appointment.getConsultationFee())
                .symptoms(appointment.getSymptoms())
                .notes(appointment.getNotes())
                .build();
    }

    public static PatientBookAppointmentDTO mapToPatientBookDTO(Appointment appointment) {
        return PatientBookAppointmentDTO.builder()
                .doctorId(appointment.getDoctor().getDoctorId())
                .patientId(appointment.getPatient().getPatientId())
                .availabilityId(appointment.getAvailability().getAvailabilityId())
                .appointmentDate(appointment.getAppointmentDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .appointmentType(appointment.getAppointmentType())
                .symptoms(appointment.getSymptoms())
                .build();
    }

}
