package com.SmartHealthcare.Impl.appointment;

import com.SmartHealthcare.constants.AppointmentStatus;
import com.SmartHealthcare.constants.PaymentStatus;
import com.SmartHealthcare.constants.ServiceCodes;
import com.SmartHealthcare.dto.request.appointment.DoctorScheduleAppointmentDTO;
import com.SmartHealthcare.dto.request.appointment.PatientBookAppointmentDTO;
import com.SmartHealthcare.exception.BusinessLogicException;
import com.SmartHealthcare.model.appointment.Appointment;
import com.SmartHealthcare.model.doctor.Doctor;
import com.SmartHealthcare.model.doctor.DoctorAvailability;
import com.SmartHealthcare.model.patient.Patient;
import com.SmartHealthcare.repository.appointment.AppointmentRepository;
import com.SmartHealthcare.repository.doctor.DoctorAvailabilityRepository;
import com.SmartHealthcare.repository.doctor.DoctorRepository;
import com.SmartHealthcare.repository.patient.PatientRepository;
import com.SmartHealthcare.service.appointment.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.SmartHealthcare.util.appoinment.AppointmentConverter;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Override
    @Transactional
    public DoctorScheduleAppointmentDTO saveAppointment(DoctorScheduleAppointmentDTO appointmentReq) {
        Doctor doctor = findDoctor(appointmentReq.getDoctorId());
        Patient patient = findPatient(appointmentReq.getPatientId());
        DoctorAvailability availability = findAndBookAvailability(
                doctor.getDoctorId(),
                appointmentReq.getStartTime(),
                appointmentReq.getEndTime()
        );

        Appointment appointment = buildAppointmentFromDoctorSchedule(
                appointmentReq, doctor, patient, availability
        );
        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentConverter.mapToDoctorScheduleDTO(saved);
    }

    @Override
    @Transactional
    public PatientBookAppointmentDTO saveAppointment(PatientBookAppointmentDTO appointmentReq) {
        Doctor doctor = findDoctor(appointmentReq.getDoctorId());
        Patient patient = findPatient(appointmentReq.getPatientId());
        DoctorAvailability availability = findAndBookAvailability(
                doctor.getDoctorId(),
                appointmentReq.getStartTime(),
                appointmentReq.getEndTime()
        );

        Appointment appointment = buildAppointmentFromPatientBooking(
                appointmentReq, doctor, patient, availability
        );
        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentConverter.mapToPatientBookDTO(saved);
    }

    private Doctor findDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new BusinessLogicException(ServiceCodes.DOCTOR_NOT_FOUND));
    }

    private Patient findPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new BusinessLogicException(ServiceCodes.PATIENT_NOT_FOUND));
    }

    private DoctorAvailability findAndBookAvailability(Long doctorId, LocalTime startTime, LocalTime endTime) {
        List<DoctorAvailability> availabilities =
                doctorAvailabilityRepository.findByDoctorDoctorId(doctorId);

        DoctorAvailability availability = availabilities.stream()
                .filter(av -> av.getStartTime().equals(startTime) && av.getEndTime().equals(endTime))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException(ServiceCodes.SLOT_NOT_AVAILABLE));

        if (availability.isBooked()) {
            throw new BusinessLogicException(ServiceCodes.DUPLICATE_APPOINTMENT);
        }

        availability.setBooked(true);
        return doctorAvailabilityRepository.save(availability);
    }

    private Appointment buildAppointmentFromDoctorSchedule(
            DoctorScheduleAppointmentDTO req,
            Doctor doctor,
            Patient patient,
            DoctorAvailability availability
    ) {
        return Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .availability(availability)
                .appointmentDate(req.getAppointmentDate())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .appointmentType(req.getAppointmentType())
                .status(req.getStatus() != null ? req.getStatus() : AppointmentStatus.SCHEDULED)
                .paymentStatus(req.getPaymentStatus() != null ? req.getPaymentStatus() : PaymentStatus.PENDING)
                .consultationFee(req.getConsultationFee())
                .symptoms(req.getSymptoms())
                .notes(req.getNotes())
                .build();
    }

    private Appointment buildAppointmentFromPatientBooking(
            PatientBookAppointmentDTO req,
            Doctor doctor,
            Patient patient,
            DoctorAvailability availability
    ) {
        return Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .availability(availability)
                .appointmentDate(req.getAppointmentDate())
                .startTime(req.getStartTime())
                .endTime(availability.getEndTime())
                .appointmentType(req.getAppointmentType())
                .status(AppointmentStatus.SCHEDULED)
                .paymentStatus(PaymentStatus.PENDING)
                .consultationFee(doctor.getConsultationFees())
                .symptoms(req.getSymptoms())
                .build();
    }

}