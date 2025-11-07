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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
            return doctorRepository.searchDoctors(specialization, location, language, minexperience, maxexperience);
        } catch (Exception e) {
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR, "Failed to search doctors: " + e.getMessage());
        }
    }

    @Override
    public void saveDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor object cannot be null");
        }
        try {
            doctorRepository.save(doctor);
        } catch (Exception e) {
            throw new ServiceException(ServiceCodes.SAVE_FAILED, "Error while saving doctor: " + e.getMessage());
        }
    }

    @Override
    public List<Doctor> getAllDoctors() {
        try {
            List<Doctor> doctors = doctorRepository.findAll();
            if (doctors.isEmpty()) {
                throw new ResourceNotFoundException(ServiceCodes.DOCTOR_NOT_FOUND, "No doctors found in the system.");
            }
            return doctors;
        } catch (Exception e) {
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR, "Failed to fetch doctor list: " + e.getMessage());
        }
    }

    @Override
    public List<Doctor> getDoctorById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }

        try {
            return Collections.singletonList(
                    doctorRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    ServiceCodes.DOCTOR_NOT_FOUND,
                                    String.format("Doctor not found with id: %d", id)))
            );
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR, "Error while fetching doctor by ID: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<Doctor> filterDoctors(FilterRequest filterRequest) {
        try {
            Double buffer = 200.0; // You can make this configurable
            Double minFee = null;
            Double maxFee = null;

            if (filterRequest.getConsultationFees() != null) {
                minFee = filterRequest.getConsultationFees() - buffer;
                maxFee = filterRequest.getConsultationFees() + buffer;
            }

            return doctorRepository.filterDoctors(
                    filterRequest.getAvailability(),
                    minFee,
                    maxFee,
                    filterRequest.getRatings()
            );
        } catch (Exception e) {
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR, "Error filtering doctors: " + e.getMessage());
        }
    }


    @Override
    public WeekSchedule getDoctorWeekSchedule(Long doctorId, LocalDate weekStart) {
        if (doctorId == null || weekStart == null) {
            throw new IllegalArgumentException("Doctor ID and weekStart date must not be null");
        }

        try {
            LocalDate weekEnd = weekStart.plusDays(6);

            List<DoctorAvailability> availabilities = availabilityRepository
                    .findByDoctorDoctorIdAndAvailableDateBetween(doctorId, weekStart, weekEnd);

            List<Appointment> appointments = appointmentRepository
                    .findByDoctorDoctorIdAndAppointmentDateBetween(doctorId, weekStart, weekEnd);

            if (availabilities.isEmpty()) {
                throw new ResourceNotFoundException(ServiceCodes.AVAILABILITY_NOT_FOUND,
                        "No availability found for this doctor for the selected week.");
            }

            // Map appointments by date + startTime for fast lookup
            Map<String, Appointment> appointmentMap = appointments.stream()
                    .collect(Collectors.toMap(
                            a -> a.getAppointmentDate() + "_" + a.getStartTime(),
                            a -> a
                    ));

            // Group availability by date
            Map<LocalDate, List<DoctorAvailability>> availByDate = availabilities.stream()
                    .collect(Collectors.groupingBy(DoctorAvailability::getAvailableDate));

            List<DaySchedule> weekSchedule = new ArrayList<>();

            for (LocalDate date = weekStart; !date.isAfter(weekEnd); date = date.plusDays(1)) {
                List<DoctorAvailability> dayAvail = availByDate.getOrDefault(date, Collections.emptyList());

                List<SlotDTO> slots = dayAvail.stream().map(slot -> {
                    String key = slot.getAvailableDate() + "_" + slot.getStartTime();
                    Appointment appt = appointmentMap.get(key);
                    if (appt != null) {
                        return new SlotDTO(slot.getStartTime(), slot.getEndTime(), "BOOKED", appt.getAppointmentId(), appt.getPatient().getPatientId());
                    } else {
                        return new SlotDTO(slot.getStartTime(), slot.getEndTime(), "FREE", null, null);
                    }
                }).collect(Collectors.toList());

                weekSchedule.add(new DaySchedule(date, slots));
            }

            return new WeekSchedule(doctorId, weekStart, weekEnd, weekSchedule);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR, "Error generating doctor schedule: " + e.getMessage());
        }
    }

    @Override
    public DoctorAvailability saveAvailability(DoctorAvailability availability) {
        if (availability == null) {
            throw new IllegalArgumentException("Availability cannot be null");
        }
        try {
            return availabilityRepository.save(availability);
        } catch (Exception e) {
            throw new ServiceException(ServiceCodes.SAVE_FAILED, "Failed to save doctor availability: " + e.getMessage());
        }
    }

    @Override
    public List<DoctorAvailabilityDTO> getAvailabilityByDoctor(Long doctorId) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }

        try {
            List<DoctorAvailability> availabilityList = availabilityRepository.findByDoctorDoctorId(doctorId);
            if (availabilityList.isEmpty()) {
                throw new ResourceNotFoundException(ServiceCodes.AVAILABILITY_NOT_FOUND,
                        "No availability found for doctor ID: " + doctorId);
            }
            return DoctorDTOConverter.convertToDTOList(availabilityList);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR, "Error fetching doctor availability: " + e.getMessage());
        }
    }
    
    @Override
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }

        try {
            List<Appointment> appointments = appointmentRepository.findByDoctorDoctorId(doctorId);
            if (appointments.isEmpty()) {
                throw new ResourceNotFoundException(ServiceCodes.APPOINTMENT_NOT_FOUND,
                        "No appointments found for doctor ID: " + doctorId);
            }
            return appointments;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR, "Error fetching appointments: " + e.getMessage());
        }
    }
}
