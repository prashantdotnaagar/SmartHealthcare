package com.SmartHealthcare.Impl.doctor;

import com.SmartHealthcare.constants.ServiceCodes;
import com.SmartHealthcare.dto.request.availability.SlotDTO;
import com.SmartHealthcare.dto.request.doctor.FilterRequest;
import com.SmartHealthcare.dto.response.doctor.DoctorAvailabilityDTO;
import com.SmartHealthcare.dto.schedule.DaySchedule;
import com.SmartHealthcare.dto.schedule.WeekSchedule;
import com.SmartHealthcare.exception.*;
import com.SmartHealthcare.model.appointment.Appointment;
import com.SmartHealthcare.model.doctor.Doctor;
import com.SmartHealthcare.model.doctor.DoctorAvailability;
import com.SmartHealthcare.repository.appointment.AppointmentRepository;
import com.SmartHealthcare.repository.doctor.DoctorAvailabilityRepository;
import com.SmartHealthcare.repository.doctor.DoctorRepository;
import com.SmartHealthcare.service.doctor.DoctorService;
import com.SmartHealthcare.util.doctor.DoctorDTOConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<Doctor> searchDoctors(String specialization, String location, String language, Integer minexperience, Integer maxexperience) {
        try {
            log.info("Searching doctors with filters - specialization: {}, location: {}, language: {}",
                    specialization, location, language);
            List<Doctor> doctors = doctorRepository.searchDoctors(specialization, location, language, minexperience, maxexperience);
            log.info("Found {} doctors matching search criteria", doctors.size());
            return doctors;
        } catch (DataAccessException e) {
            log.error("Database error while searching doctors: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while searching doctors: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDoctor(Doctor doctor) {
        if (doctor == null) {
            log.warn("Attempted to save null doctor object");
            throw new IllegalArgumentException("Doctor object cannot be null");
        }
        try {
            log.info("Saving doctor: {}", doctor.getFullName());
            doctorRepository.save(doctor);
            log.info("Successfully saved doctor with ID: {}", doctor.getDoctorId());
        } catch (DataAccessException e) {
            log.error("Database error while saving doctor: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while saving doctor: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.SAVE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> getAllDoctors() {
        try {
            log.info("Fetching all doctors");
            List<Doctor> doctors = doctorRepository.findAll();
            if (doctors.isEmpty()) {
                log.warn("No doctors found in the system");
                throw new ResourceNotFoundException(ServiceCodes.DOCTOR_NOT_FOUND);
            }
            log.info("Retrieved {} doctors", doctors.size());
            return doctors;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while fetching all doctors: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while fetching doctors: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> getDoctorById(Long id) {
        if (id == null || id <= 0) {
            log.warn("Invalid doctor ID provided: {}", id);
            throw new IllegalArgumentException("Doctor ID must be a positive number");
        }

        try {
            log.info("Fetching doctor with ID: {}", id);
            Doctor doctor = doctorRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Doctor not found with ID: {}", id);
                        return new ResourceNotFoundException(ServiceCodes.DOCTOR_NOT_FOUND);
                    });
            log.info("Successfully retrieved doctor: {}", doctor.getFullName());
            return Collections.singletonList(doctor);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while fetching doctor with ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while fetching doctor with ID {}: {}", id, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctors(FilterRequest filterRequest) {
        if (filterRequest == null) {
            log.warn("Filter request is null");
            throw new IllegalArgumentException("Filter request cannot be null");
        }

        try {
            log.info("Filtering doctors with criteria: {}", filterRequest);
            Double buffer = 200.0;
            Double minFee = null;
            Double maxFee = null;

            if (filterRequest.getConsultationFees() != null) {
                minFee = filterRequest.getConsultationFees() - buffer;
                maxFee = filterRequest.getConsultationFees() + buffer;
            }

            List<Doctor> filteredDoctors = doctorRepository.filterDoctors(
                    filterRequest.getAvailability(),
                    minFee,
                    maxFee,
                    filterRequest.getRatings()
            );
            log.info("Found {} doctors matching filter criteria", filteredDoctors.size());
            return filteredDoctors;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while filtering doctors: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while filtering doctors: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public WeekSchedule getDoctorWeekSchedule(Long doctorId, LocalDate weekStart) {
        if (doctorId == null || doctorId <= 0) {
            log.warn("Invalid doctor ID: {}", doctorId);
            throw new IllegalArgumentException("Doctor ID must be a positive number");
        }
        if (weekStart == null) {
            log.warn("Week start date is null");
            throw new IllegalArgumentException("Week start date must not be null");
        }

        try {
            log.info("Fetching week schedule for doctor ID: {} starting from {}", doctorId, weekStart);
            LocalDate weekEnd = weekStart.plusDays(6);

            List<DoctorAvailability> availabilities = availabilityRepository
                    .findByDoctorDoctorIdAndAvailableDateBetween(doctorId, weekStart, weekEnd);

            List<Appointment> appointments = appointmentRepository
                    .findByDoctorDoctorIdAndAppointmentDateBetween(doctorId, weekStart, weekEnd);

            if (availabilities.isEmpty()) {
                log.warn("No availability found for doctor ID: {} for week starting {}", doctorId, weekStart);
                throw new ResourceNotFoundException(ServiceCodes.AVAILABILITY_NOT_FOUND);
            }

            Map<String, Appointment> appointmentMap = appointments.stream()
                    .collect(Collectors.toMap(
                            a -> a.getAppointmentDate() + "_" + a.getStartTime(),
                            a -> a,
                            (existing, replacement) -> existing
                    ));

            Map<LocalDate, List<DoctorAvailability>> availByDate = availabilities.stream()
                    .collect(Collectors.groupingBy(DoctorAvailability::getAvailableDate));

            List<DaySchedule> weekSchedule = new ArrayList<>();

            for (LocalDate date = weekStart; !date.isAfter(weekEnd); date = date.plusDays(1)) {
                List<DoctorAvailability> dayAvail = availByDate.getOrDefault(date, Collections.emptyList());

                List<SlotDTO> slots = dayAvail.stream().map(slot -> {
                    String key = slot.getAvailableDate() + "_" + slot.getStartTime();
                    Appointment appt = appointmentMap.get(key);
                    if (appt != null) {
                        return new SlotDTO(slot.getStartTime(), slot.getEndTime(), "BOOKED",
                                appt.getAppointmentId(), appt.getPatient().getPatientId());
                    } else {
                        return new SlotDTO(slot.getStartTime(), slot.getEndTime(), "FREE", null, null);
                    }
                }).collect(Collectors.toList());

                weekSchedule.add(new DaySchedule(date, slots));
            }

            log.info("Successfully generated week schedule for doctor ID: {}", doctorId);
            return new WeekSchedule(doctorId, weekStart, weekEnd, weekSchedule);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while generating doctor schedule for ID {}: {}", doctorId, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while generating doctor schedule for ID {}: {}", doctorId, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.SCHEDULE_GENERATION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DoctorAvailability saveAvailability(DoctorAvailability availability) {
        if (availability == null) {
            log.warn("Attempted to save null availability object");
            throw new IllegalArgumentException("Availability cannot be null");
        }
        try {
            log.info("Saving doctor availability for doctor ID: {}", availability.getDoctor().getDoctorId());
            DoctorAvailability saved = availabilityRepository.save(availability);
            log.info("Successfully saved availability with ID: {}", saved.getAvailabilityId());
            return saved;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while saving availability: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while saving availability: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.AVAILABILITY_SAVE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAvailabilityDTO> getAvailabilityByDoctor(Long doctorId) {
        if (doctorId == null || doctorId <= 0) {
            log.warn("Invalid doctor ID provided: {}", doctorId);
            throw new IllegalArgumentException("Doctor ID must be a positive number");
        }

        try {
            log.info("Fetching availability for doctor ID: {}", doctorId);
            List<DoctorAvailability> availabilityList = availabilityRepository.findByDoctorDoctorId(doctorId);
            if (availabilityList.isEmpty()) {
                log.warn("No availability found for doctor ID: {}", doctorId);
                throw new ResourceNotFoundException(ServiceCodes.AVAILABILITY_NOT_FOUND);
            }
            log.info("Retrieved {} availability slots for doctor ID: {}", availabilityList.size(), doctorId);
            return DoctorDTOConverter.convertToDTOList(availabilityList);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while fetching availability for doctor ID {}: {}", doctorId, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while fetching availability for doctor ID {}: {}", doctorId, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        if (doctorId == null || doctorId <= 0) {
            log.warn("Invalid doctor ID provided: {}", doctorId);
            throw new IllegalArgumentException("Doctor ID must be a positive number");
        }

        try {
            log.info("Fetching appointments for doctor ID: {}", doctorId);
            List<Appointment> appointments = appointmentRepository.findByDoctorDoctorId(doctorId);
            if (appointments.isEmpty()) {
                log.warn("No appointments found for doctor ID: {}", doctorId);
                throw new ResourceNotFoundException(ServiceCodes.APPOINTMENT_NOT_FOUND);
            }
            log.info("Retrieved {} appointments for doctor ID: {}", appointments.size(), doctorId);
            return appointments;
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while fetching appointments for doctor ID {}: {}", doctorId, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while fetching appointments for doctor ID {}: {}", doctorId, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR);
        }
    }
}