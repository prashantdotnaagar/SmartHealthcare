package com.SmartHealthcare.service.appointment;

import com.SmartHealthcare.dto.request.appointment.CancelAppointment;
import com.SmartHealthcare.dto.request.appointment.DoctorScheduleAppointmentDTO;
import com.SmartHealthcare.dto.request.appointment.PatientBookAppointmentDTO;


public interface AppointmentService {
    DoctorScheduleAppointmentDTO saveAppointment(DoctorScheduleAppointmentDTO appointment);
    PatientBookAppointmentDTO saveAppointment(PatientBookAppointmentDTO appointment);
    CancelAppointment cancelAppointment(CancelAppointment cancelllationReq);
}
