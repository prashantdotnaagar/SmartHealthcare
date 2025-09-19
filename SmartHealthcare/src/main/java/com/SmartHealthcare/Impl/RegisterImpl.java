package com.SmartHealthcare.Impl;

import com.SmartHealthcare.dto.RegiesterRequest;
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

    public void registerUser(RegiesterRequest request){
        if(userService.getUserByEmail(request.getUserEmail()) != null){
            throw new RuntimeException("User with this email already exist.");
        }

        User user = User.builder()
                .userName(request.getUserName())
                .role(request.getRole())
                .userEmail(request.getUserEmail())
                .userPassword(passwordEncoder.encode(request.getUserPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            userService.saveUser(user);

        switch (request.getRole()) {
            case ADMIN:
                Admin admin=Admin.builder()
                        .accessLevel(request.getAccessLevel())
                        .department(request.getDepartment())
                        .user(user)
                        .updatedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .build();
                adminService.saveAdmin(admin);
                break;
            case DOCTOR:
                Doctor doctor=Doctor.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .licenseNumber(request.getLicenseNumber())
                        .phone(request.getPhoneNumber())
                        .specialization(request.getSpecialization())
                        .user(user)
                        .updatedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .build();
                doctorService.saveDoctor(doctor);
                break;
            case PATIENT:
                Patient patient=Patient.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .phoneNumber(request.getPhoneNumber())
                        .gender(request.getGender())
                        .emergencyPhoneNumber(request.getEmergencyPhoneNumber())
                        .dateOfBirth(request.getDateOfBirth())
                        .address(request.getAddress())
                        .updatedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .user(user)
                        .build();
                patientService.savePatient(patient);
                break;

        }
    }
}
