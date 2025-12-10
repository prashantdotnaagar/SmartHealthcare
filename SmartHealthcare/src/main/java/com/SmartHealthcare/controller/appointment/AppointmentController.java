package com.SmartHealthcare.controller.appointment;

import com.SmartHealthcare.constants.ServiceCodes;
import com.SmartHealthcare.dto.request.appointment.CancelAppointment;
import com.SmartHealthcare.dto.request.appointment.DoctorScheduleAppointmentDTO;
import com.SmartHealthcare.dto.request.appointment.PatientBookAppointmentDTO;
import com.SmartHealthcare.service.appointment.AppointmentService;
import com.SmartHealthcare.service.doctor.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    DoctorService doctorService;


    @PostMapping("/doctor")
    public ResponseEntity<DoctorScheduleAppointmentDTO> createDoctorAppointment(@RequestBody @Valid DoctorScheduleAppointmentDTO appointmentReq) {
        DoctorScheduleAppointmentDTO appointment = appointmentService.saveAppointment(appointmentReq);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/patient")
    public ResponseEntity<PatientBookAppointmentDTO>createPatientAppointment(@RequestBody @Valid PatientBookAppointmentDTO appointmentReq){
        PatientBookAppointmentDTO appointment =appointmentService.saveAppointment(appointmentReq);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String>cancelAppointment(@RequestBody @Valid CancelAppointment cancellationReq){
        appointmentService.cancelAppointment(cancellationReq);
        return ResponseEntity.ok(ServiceCodes.APPOINTMENT_CANCELLED.getMessage());
    }


}
