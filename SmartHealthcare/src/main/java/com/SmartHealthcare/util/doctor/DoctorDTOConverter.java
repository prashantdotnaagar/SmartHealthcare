package com.SmartHealthcare.util.doctor;

import com.SmartHealthcare.dto.response.doctor.DoctorDTO;;
import com.SmartHealthcare.dto.response.doctor.DoctorAvailabilityDTO;
import com.SmartHealthcare.model.doctor.Doctor;
import com.SmartHealthcare.model.doctor.DoctorAvailability;

import java.util.List;
import java.util.stream.Collectors;

public class DoctorDTOConverter {

    public static DoctorDTO convertToDoctorDTO(Doctor doctor) {
        if (doctor == null) return null;

        return DoctorDTO.builder()
                .doctorId(doctor.getDoctorId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .phone(doctor.getPhone())
                .qualifications(doctor.getQualifications())
                .location(doctor.getLocation())
                .language(doctor.getLanguage())
                .experience(doctor.getExperience())
                .availability(doctor.getAvailability())
                .consultationFees(doctor.getConsultationFees())
                .ratings(doctor.getRatings())
                .reviews(doctor.getReviews())
                .appointmentIds(
                        doctor.getAppointments() != null
                                ? doctor.getAppointments().stream()
                                .map(a -> a.getAppointmentId())
                                .collect(Collectors.toList())
                                : null
                )
                .build();
    }

    public static DoctorAvailabilityDTO convertToAvailabilityDTO(DoctorAvailability availability){
        if (availability == null) return null;

        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        dto.setAvailabilityId(availability.getAvailabilityId());
        dto.setAvailableDate(availability.getAvailableDate());
        dto.setStartTime(availability.getStartTime());
        dto.setEndTime(availability.getEndTime());
        return dto;
    }
    public static List<DoctorAvailabilityDTO> convertToDTOList(List<DoctorAvailability> availabilityList) {
        if (availabilityList == null || availabilityList.isEmpty()) return List.of();

        return availabilityList.stream()
                .map(DoctorDTOConverter::convertToAvailabilityDTO)
                .collect(Collectors.toList());
    }

    public static List<DoctorDTO> convertToDoctorDTOList(List<Doctor> doctors) {
        if (doctors == null || doctors.isEmpty()) return List.of();
        return doctors.stream()
                .map(DoctorDTOConverter::convertToDoctorDTO)
                .collect(Collectors.toList());
    }
}
