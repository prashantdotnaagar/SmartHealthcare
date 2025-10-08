package com.SmartHealthcare.Impl;

import com.SmartHealthcare.dto.request.admin.RegisterRequestAdmin;
import com.SmartHealthcare.dto.request.doctor.RegisterRequestDoctor;
import com.SmartHealthcare.dto.request.patient.RegisterRequestPatient;
import com.SmartHealthcare.dto.request.user.BaseUserRequest;
import com.SmartHealthcare.dto.request.user.RegisterRequestUser;
import com.SmartHealthcare.model.Admin;
import com.SmartHealthcare.model.Doctor;
import com.SmartHealthcare.model.Patient;
import com.SmartHealthcare.model.User;
import com.SmartHealthcare.service.AdminService;
import com.SmartHealthcare.service.DoctorService;
import com.SmartHealthcare.service.PatientService;
import com.SmartHealthcare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
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


    // ✅ Generic user creation method
    private User createBaseUser(BaseUserRequest request) {
        if (userService.getUserByEmail(request.getUserEmail()) != null) {
            throw new RuntimeException("User with this email already exists.");
        }

        return userService.saveUser(
                User.builder()
                        .userName(request.getUserName())
                        .role(request.getRole())
                        .userEmail(request.getUserEmail())
                        .userPassword(passwordEncoder.encode(request.getUserPassword()))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

    }



    // ✅ Register Admin
    public void registerAdmin(RegisterRequestAdmin request) {
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
    }


    // ✅ Register Doctor
    public void registerDoctor(RegisterRequestDoctor request) {
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
    }


    // ✅ Register Patient
    public void registerPatient(RegisterRequestPatient request) {
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
    }
}
