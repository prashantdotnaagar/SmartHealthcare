package com.SmartHealthcare.Impl.registration;

import com.SmartHealthcare.constants.ServiceCodes;
import com.SmartHealthcare.dto.request.admin.RegisterRequestAdmin;
import com.SmartHealthcare.dto.request.doctor.RegisterRequestDoctor;
import com.SmartHealthcare.dto.request.patient.RegisterRequestPatient;
import com.SmartHealthcare.dto.request.user.BaseUserRequest;
import com.SmartHealthcare.exception.DuplicateResourceException;
import com.SmartHealthcare.exception.ServiceException;
import com.SmartHealthcare.model.admin.Admin;
import com.SmartHealthcare.model.doctor.Doctor;
import com.SmartHealthcare.model.patient.Patient;
import com.SmartHealthcare.model.user.User;
import com.SmartHealthcare.service.admin.AdminService;
import com.SmartHealthcare.service.doctor.DoctorService;
import com.SmartHealthcare.service.patient.PatientService;
import com.SmartHealthcare.service.user.UserService;
import com.SmartHealthcare.util.doctor.DoctorHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class RegisterImpl {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DoctorHelper doctorHelper;

    @Transactional
    private User createBaseUser(BaseUserRequest request) {
        if (userService.getUserByEmail(request.getUserEmail()) != null) {
            log.warn("Duplicate user registration attempt: {}", request.getUserEmail());
            throw new DuplicateResourceException(
                    ServiceCodes.RESOURCE_CONFLICT
            );
        }

        try {
            User user = userService.saveUser(
                    User.builder()
                            .userName(request.getUserName())
                            .role(request.getRole())
                            .userEmail(request.getUserEmail())
                            .userPassword(passwordEncoder.encode(request.getUserPassword()))
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build()
            );
            log.info("User created successfully: {}", request.getUserEmail());
            return user;
        } catch (Exception e) {
            log.error("Failed to create user: {}", request.getUserEmail(), e);
            throw new ServiceException(ServiceCodes.SAVE_FAILED);
        }
    }

    @Transactional
    public void registerAdmin(RegisterRequestAdmin request) {
        try {
            log.info("Admin registration started: {}", request.getUserEmail());
            User user = createBaseUser(request);

            Admin admin = Admin.builder()
                    .accessLevel(request.getAccessLevel())
                    .department(request.getDepartment())
                    .user(user)
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            adminService.saveAdmin(admin);
            log.info("Admin registered successfully: {}", request.getUserEmail());
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Admin registration failed: {}", request.getUserEmail(), e);
            throw new ServiceException(ServiceCodes.SAVE_FAILED);
        }
    }

    @Transactional
    public void registerDoctor(RegisterRequestDoctor request) {
        try {
            log.info("Doctor registration started: {}", request.getUserEmail());
            User user = createBaseUser(request);

            Doctor doctor = Doctor.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .licenseNumber(request.getLicenseNumber())
                    .phone(request.getPhone())
                    .specialization(request.getSpecialization())
                    .qualifications(request.getQualifications())
                    .location(request.getLocation())
                    .language(request.getLanguage())
                    .experience(request.getExperience() != null ? request.getExperience() : 0)
                    .availability(request.getAvailability())
                    .consultationFees(request.getConsultationFees() != null ? request.getConsultationFees() : 0.0)
                    .ratings(BigDecimal.valueOf(request.getRatings() != null ? request.getRatings() : 0.0))
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            doctorService.saveDoctor(doctor);
            doctorHelper.populateDoctorAvailability(doctor, request.getAvailability(), request.getAvailabilityStarting(), request.getAvailabilityEnd());
            log.info("Doctor registered successfully: {}", request.getUserEmail());
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Doctor registration failed: {}", request.getUserEmail(), e);
            throw new ServiceException(ServiceCodes.DOCTOR_SAVE_FAILED);
        }
    }

    @Transactional
    public void registerPatient(RegisterRequestPatient request) {
        try {
            log.info("Patient registration started: {}", request.getUserEmail());
            User user = createBaseUser(request);

            Patient patient = Patient.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phoneNumber(request.getPhoneNumber())
                    .gender(request.getGender())
                    .emergencyPhoneNumber(request.getEmergencyPhoneNumber())
                    .dateOfBirth(request.getDateOfBirth())
                    .address(request.getAddress())
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            patientService.savePatient(patient);
            log.info("Patient registered successfully: {}", request.getUserEmail());
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Patient registration failed: {}", request.getUserEmail(), e);
            throw new ServiceException(ServiceCodes.PATIENT_SAVE_FAILED);
        }
    }
}