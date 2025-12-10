package com.SmartHealthcare.Impl.appointment;

import com.SmartHealthcare.constants.AppointmentStatus;
import com.SmartHealthcare.constants.PaymentStatus;
import com.SmartHealthcare.constants.ServiceCodes;
import com.SmartHealthcare.dto.request.appointment.CancelAppointment;
import com.SmartHealthcare.dto.request.appointment.DoctorScheduleAppointmentDTO;
import com.SmartHealthcare.dto.request.appointment.PatientBookAppointmentDTO;
import com.SmartHealthcare.exception.BusinessLogicException;
import com.SmartHealthcare.exception.ResourceNotFoundException;
import com.SmartHealthcare.exception.DuplicateResourceException;
import com.SmartHealthcare.exception.ServiceException;
import com.SmartHealthcare.model.appointment.Appointment;
import com.SmartHealthcare.model.doctor.Doctor;
import com.SmartHealthcare.model.doctor.DoctorAvailability;
import com.SmartHealthcare.model.patient.Patient;
import com.SmartHealthcare.repository.appointment.AppointmentRepository;
import com.SmartHealthcare.repository.doctor.DoctorAvailabilityRepository;
import com.SmartHealthcare.repository.doctor.DoctorRepository;
import com.SmartHealthcare.repository.patient.PatientRepository;
import com.SmartHealthcare.service.appointment.AppointmentService;
import com.SmartHealthcare.util.appoinment.AppointmentConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DoctorScheduleAppointmentDTO saveAppointment(DoctorScheduleAppointmentDTO appointmentReq) {
        try {
            log.info("Processing doctor scheduled appointment for doctorId: {}, patientId: {}",
                    appointmentReq.getDoctorId(), appointmentReq.getPatientId());

            validateAppointmentRequest(appointmentReq);

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

            Appointment saved = saveAppointmentEntity(appointment);
            log.info("Successfully created appointment with ID: {}", saved.getAppointmentId());

            return AppointmentConverter.mapToDoctorScheduleDTO(saved);

        } catch (ResourceNotFoundException | BusinessLogicException | DuplicateResourceException e) {
            log.error("Business logic error while creating doctor scheduled appointment: {}", e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while saving appointment: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while creating appointment: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.APPOINTMENT_SAVE_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PatientBookAppointmentDTO saveAppointment(PatientBookAppointmentDTO appointmentReq) {
        try {
            log.info("Processing patient booking appointment for doctorId: {}, patientId: {}",
                    appointmentReq.getDoctorId(), appointmentReq.getPatientId());

            validatePatientBookingRequest(appointmentReq);

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

            Appointment saved = saveAppointmentEntity(appointment);
            log.info("Successfully created patient appointment with ID: {}", saved.getAppointmentId());

            return AppointmentConverter.mapToPatientBookDTO(saved);

        } catch (ResourceNotFoundException | BusinessLogicException | DuplicateResourceException e) {
            log.error("Business logic error while creating patient appointment: {}", e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while saving appointment: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while creating patient appointment: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.APPOINTMENT_SAVE_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CancelAppointment cancelAppointment(CancelAppointment cancellationReq) {
        try {
            log.info("Processing appointment cancellation for ID: {}", cancellationReq.getAppointmentId());

            Appointment appointment = appointmentRepository.findById(cancellationReq.getAppointmentId())
                    .orElseThrow(() -> {
                        log.warn("Appointment not found with ID: {}", cancellationReq.getAppointmentId());
                        return new ResourceNotFoundException(ServiceCodes.APPOINTMENT_NOT_FOUND);
                    });

            if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
                log.warn("Appointment with ID {} is already cancelled", cancellationReq.getAppointmentId());
                throw new BusinessLogicException(ServiceCodes.APPOINTMENT_ALREADY_CANCELLED);
            }


            appointment.setCancellationReason(cancellationReq.getCancellationReason());
            appointment.setCancelledBy(cancellationReq.getCancelledBy());
            appointment.setCancelledAt(cancellationReq.getCancelledAt());
            appointment.setStatus(AppointmentStatus.CANCELLED);


            DoctorAvailability availability = appointment.getAvailability();
            if (availability != null) {
                availability.setBooked(false);
                availability.setUpdatedAt(java.time.LocalDateTime.now());
                doctorAvailabilityRepository.save(availability);
                log.info("Freed doctor slot for availability ID: {}", availability.getAvailabilityId());
            } else {
                log.warn("No availability linked with appointment ID: {}", appointment.getAppointmentId());
            }


            appointmentRepository.save(appointment);
            log.info("Successfully cancelled appointment with ID: {}", appointment.getAppointmentId());

            return cancellationReq;

        } catch (ResourceNotFoundException | BusinessLogicException e) {
            log.error("Business error while cancelling appointment: {}", e.getMessage());
            throw e;
        } catch (org.springframework.dao.DataAccessException e) {
            log.error("Database error while cancelling appointment: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while cancelling appointment: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.APPOINTMENT_CANCEL_FAILED);
        }
    }


    private void validateAppointmentRequest(DoctorScheduleAppointmentDTO req) {
        if (req.getDoctorId() == null || req.getDoctorId() <= 0) {
            throw new IllegalArgumentException("Invalid doctor ID");
        }
        if (req.getPatientId() == null || req.getPatientId() <= 0) {
            throw new IllegalArgumentException("Invalid patient ID");
        }
        validateAppointmentDateTime(req.getAppointmentDate(), req.getStartTime(), req.getEndTime());

        if (req.getConsultationFee() != null && req.getConsultationFee() < 0) {
            throw new IllegalArgumentException("Consultation fee cannot be negative");
        }
    }

    private void validatePatientBookingRequest(PatientBookAppointmentDTO req) {
        if (req.getDoctorId() == null || req.getDoctorId() <= 0) {
            throw new IllegalArgumentException("Invalid doctor ID");
        }
        if (req.getPatientId() == null || req.getPatientId() <= 0) {
            throw new IllegalArgumentException("Invalid patient ID");
        }
        validateAppointmentDateTime(req.getAppointmentDate(), req.getStartTime(), req.getEndTime());
    }

    private void validateAppointmentDateTime(LocalDate appointmentDate, LocalTime startTime, LocalTime endTime) {
        if (appointmentDate == null) {
            throw new IllegalArgumentException("Appointment date is required");
        }
        if (appointmentDate.isBefore(LocalDate.now())) {
            throw new BusinessLogicException(ServiceCodes.INVALID_APPOINTMENT_DATE);
        }
        if (startTime == null) {
            throw new IllegalArgumentException("Start time is required");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("End time is required");
        }
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    private Doctor findDoctor(Long doctorId) {
        try {
            return doctorRepository.findById(doctorId)
                    .orElseThrow(() -> {
                        log.warn("Doctor not found with ID: {}", doctorId);
                        return new ResourceNotFoundException(ServiceCodes.DOCTOR_NOT_FOUND);
                    });
        } catch (DataAccessException e) {
            log.error("Database error while fetching doctor with ID {}: {}", doctorId, e.getMessage());
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        }
    }

    private Patient findPatient(Long patientId) {
        try {
            return patientRepository.findById(patientId)
                    .orElseThrow(() -> {
                        log.warn("Patient not found with ID: {}", patientId);
                        return new ResourceNotFoundException(ServiceCodes.PATIENT_NOT_FOUND);
                    });
        } catch (DataAccessException e) {
            log.error("Database error while fetching patient with ID {}: {}", patientId, e.getMessage());
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        }
    }

    private DoctorAvailability findAndBookAvailability(Long doctorId, LocalTime startTime, LocalTime endTime) {
        try {
            List<DoctorAvailability> availabilities =
                    doctorAvailabilityRepository.findByDoctorDoctorId(doctorId);

            if (availabilities.isEmpty()) {
                log.warn("No availability found for doctor ID: {}", doctorId);
                throw new ResourceNotFoundException(ServiceCodes.DOCTOR_AVAILABILITY_NOT_FOUND);
            }

            DoctorAvailability availability = availabilities.stream()
                    .filter(av -> av.getStartTime().equals(startTime) && av.getEndTime().equals(endTime))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.warn("Requested slot not available for doctor ID: {} at time: {} - {}",
                                doctorId, startTime, endTime);
                        return new BusinessLogicException(ServiceCodes.SLOT_NOT_AVAILABLE);
                    });

            if (availability.isBooked()) {
                log.warn("Slot already booked for doctor ID: {} at time: {} - {}",
                        doctorId, startTime, endTime);
                throw new DuplicateResourceException(ServiceCodes.DUPLICATE_APPOINTMENT);
            }

            availability.setBooked(true);
            return doctorAvailabilityRepository.save(availability);

        } catch (DuplicateResourceException | BusinessLogicException | ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while managing availability: {}", e.getMessage());
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while managing availability: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.AVAILABILITY_UPDATE_FAILED);
        }
    }

    private Appointment saveAppointmentEntity(Appointment appointment) {
        try {
            return appointmentRepository.save(appointment);
        } catch (DataAccessException e) {
            log.error("Database error while saving appointment entity: {}", e.getMessage());
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while saving appointment: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.APPOINTMENT_SAVE_FAILED);
        }
    }

    private Appointment buildAppointmentFromDoctorSchedule(
            DoctorScheduleAppointmentDTO req,
            Doctor doctor,
            Patient patient,
            DoctorAvailability availability
    ) {
        try {
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
        } catch (Exception e) {
            log.error("Error building appointment from doctor schedule: {}", e.getMessage());
            throw new ServiceException(ServiceCodes.APPOINTMENT_SAVE_FAILED);
        }
    }

    private Appointment buildAppointmentFromPatientBooking(
            PatientBookAppointmentDTO req,
            Doctor doctor,
            Patient patient,
            DoctorAvailability availability
    ) {
        try {
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
        } catch (Exception e) {
            log.error("Error building appointment from patient booking: {}", e.getMessage());
            throw new ServiceException(ServiceCodes.APPOINTMENT_SAVE_FAILED);
        }
    }
}