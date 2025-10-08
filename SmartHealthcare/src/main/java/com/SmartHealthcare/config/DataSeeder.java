package com.SmartHealthcare.config;

import com.SmartHealthcare.model.*;
import com.SmartHealthcare.service.AdminService;
import com.SmartHealthcare.service.DoctorService;
import com.SmartHealthcare.service.PatientService;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataSeeder {

    @Autowired
    private AdminService adminService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    private final String[] specialties = {
            "Cardiology", "Neurology", "Dermatology", "Orthopedics",
            "Pediatrics", "General Surgery", "Psychiatry"
    };
/**
 Uncomment and run if want to add dummy data in the Database
 */
    @PostConstruct
    @Transactional
//    public void seedData() {
//        seedAdmins(50);
//        seedDoctors(100);
//        seedPatients(100);
//    }

    private void seedAdmins(int count) {
        for (int i = 1; i <= count; i++) {
            User user = User.builder()
                    .userName("admin" + i)
                    .userEmail("admin" + i + "@example.com")
                    .userPassword(passwordEncoder.encode("Admin@123"))
                    .role(UserRole.ADMIN)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Admin admin = Admin.builder()
                    .user(user) // cascades automatically
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .department(faker.company().industry())
                    .accessLevel(random.nextInt(5) + 1)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            adminService.saveAdmin(admin);
        }
    }

    private void seedDoctors(int count) {
        for (int i = 1; i <= count; i++) {
            User user = User.builder()
                    .userName("doctor" + i)
                    .userEmail("doctor" + i + "@example.com")
                    .userPassword(passwordEncoder.encode("Doctor@123"))
                    .role(UserRole.DOCTOR)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            String specialization = specialties[random.nextInt(specialties.length)];

            Doctor doctor = Doctor.builder()
                    .user(user) // cascades automatically
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .licenseNumber("LIC" + faker.number().digits(6))
                    .phone(faker.phoneNumber().cellPhone())
                    .specialization(specialization)
                    .qualifications("MBBS, MD")
                    .location(faker.address().city())
                    .language("English")
                    .experience(random.nextInt(30))
                    .availability("Mon-Fri, 9AM-5PM")
                    .consultationFees((double) (random.nextInt(1000) + 500))
                    .ratings(BigDecimal.valueOf(Math.round(random.nextDouble() * 50) / 10.0))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            doctorService.saveDoctor(doctor);
        }
    }

    private void seedPatients(int count) {
        for (int i = 1; i <= count; i++) {
            User user = User.builder()
                    .userName("patient" + i)
                    .userEmail("patient" + i + "@example.com")
                    .userPassword(passwordEncoder.encode("Patient@123"))
                    .role(UserRole.PATIENT)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Patient patient = Patient.builder()
                    .user(user) // cascades automatically
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .dateOfBirth(faker.date().birthday().toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate())
                    .gender(random.nextBoolean() ? Gender.MALE : Gender.FEMALE)
                    .phoneNumber(faker.phoneNumber().cellPhone())
                    .address(faker.address().fullAddress())
                    .emergencyPhoneNumber(faker.phoneNumber().cellPhone())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            patientService.savePatient(patient);
        }
    }
}




