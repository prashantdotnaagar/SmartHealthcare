//package com.SmartHealthcare.config;
//
//import com.SmartHealthcare.constants.*;
//import com.SmartHealthcare.model.admin.Admin;
//import com.SmartHealthcare.model.appointment.Appointment;
//import com.SmartHealthcare.model.doctor.Doctor;
//import com.SmartHealthcare.model.doctor.DoctorAvailability;
//import com.SmartHealthcare.model.patient.Patient;
//import com.SmartHealthcare.model.user.User;
//import com.SmartHealthcare.service.admin.AdminService;
//import com.SmartHealthcare.service.appointment.AppointmentService;
//import com.SmartHealthcare.service.doctor.DoctorService;
//import com.SmartHealthcare.service.patient.PatientService;
//import com.github.javafaker.Faker;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Random;
//
//@Component
//public class DataSeeder {
//
//    @Autowired
//    private AdminService adminService;
//
//    @Autowired
//    private AppointmentService appointmentService;
//
//    @Autowired
//    private DoctorService doctorService;
//
//    @Autowired
//    private PatientService patientService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    private final Faker faker = new Faker();
//    private final Random random = new Random();
//
//    private final String[] specialties = {
//            "Cardiology", "Neurology", "Dermatology", "Orthopedics",
//            "Pediatrics", "General Surgery", "Psychiatry"
//    };
///**
// Uncomment and run if want to add dummy data in the Database
// */
////    @PostConstruct
////    @Transactional
////    public void seedData() {
////        seedAdmins(50);
////        seedDoctors(100);
////        seedPatients(100);
////        seedDoctorAvailability(7);
////        seedAppointments(50);
////    }
//    private void seedAppointments(int count) {
//        var doctors = doctorService.getAllDoctors();
//        var patients = patientService.getAllPatients();
//
//        for (int i = 0; i < count; i++) {
//            Doctor doctor = doctors.get(random.nextInt(doctors.size()));
//            Patient patient = patients.get(random.nextInt(patients.size()));
//
//            // Get a random available slot for this doctor
//            var availList = doctorService.getAvailabilityByDoctor(doctor.getDoctorId())
//                    .stream()
//                    .filter(a -> !a.isBooked())
//                    .toList();
//
//            if (availList.isEmpty()) continue;
//
//            DoctorAvailability slot = availList.get(random.nextInt(availList.size()));
//
//            Appointment appointment = Appointment.builder()
//                    .doctor(doctor)
//                    .patient(patient)
//                    .availability(slot)
//                    .appointmentDate(slot.getAvailableDate())
//                    .startTime(slot.getStartTime())
//                    .endTime(slot.getEndTime())
//                    .appointmentType(AppointmentType.TELECONSULTATION)
//                    .status(AppointmentStatus.SCHEDULED)
//                    .consultationFee(doctor.getConsultationFees())
//                    .paymentStatus(PaymentStatus.PENDING)
//                    .symptoms("Sample symptoms")
//                    .notes("Sample notes")
//                    .reminderSent(false)
//                    .build();
//
//            appointmentService.saveAppointment(appointment);
//
//            slot.setBooked(true);
//            doctorService.saveAvailability(slot);
//        }
//    }
//
//
//    private void seedAdmins(int count) {
//        for (int i = 1; i <= count; i++) {
//            User user = User.builder()
//                    .userName("admin" + i)
//                    .userEmail("admin" + i + "@example.com")
//                    .userPassword(passwordEncoder.encode("Admin@123"))
//                    .role(UserRole.ADMIN)
//                    .createdAt(LocalDateTime.now())
//                    .updatedAt(LocalDateTime.now())
//                    .build();
//
//            Admin admin = Admin.builder()
//                    .user(user) // cascades automatically
//                    .firstName(faker.name().firstName())
//                    .lastName(faker.name().lastName())
//                    .department(faker.company().industry())
//                    .accessLevel(random.nextInt(5) + 1)
//                    .createdAt(LocalDateTime.now())
//                    .updatedAt(LocalDateTime.now())
//                    .build();
//
//            adminService.saveAdmin(admin);
//        }
//    }
//
//    private void seedDoctors(int count) {
//        for (int i = 1; i <= count; i++) {
//            User user = User.builder()
//                    .userName("doctor" + i)
//                    .userEmail("doctor" + i + "@example.com")
//                    .userPassword(passwordEncoder.encode("Doctor@123"))
//                    .role(UserRole.DOCTOR)
//                    .createdAt(LocalDateTime.now())
//                    .updatedAt(LocalDateTime.now())
//                    .build();
//
//            String specialization = specialties[random.nextInt(specialties.length)];
//
//            Doctor doctor = Doctor.builder()
//                    .user(user) // cascades automatically
//                    .firstName(faker.name().firstName())
//                    .lastName(faker.name().lastName())
//                    .licenseNumber("LIC" + faker.number().digits(6))
//                    .phone(faker.phoneNumber().cellPhone())
//                    .specialization(specialization)
//                    .qualifications("MBBS, MD")
//                    .location(faker.address().city())
//                    .language("English")
//                    .experience(random.nextInt(30))
//                    .availability("Mon-Fri, 9AM-5PM")
//                    .consultationFees((double) (random.nextInt(1000) + 500))
//                    .ratings(BigDecimal.valueOf(Math.round(random.nextDouble() * 50) / 10.0))
//                    .createdAt(LocalDateTime.now())
//                    .updatedAt(LocalDateTime.now())
//                    .build();
//
//            doctorService.saveDoctor(doctor);
//        }
//    }
//    private void seedDoctorAvailability(int daysPerDoctor) {
//        doctorService.getAllDoctors().forEach(doctor -> {
//            LocalDate startDate = LocalDate.now();
//            for (int i = 0; i < daysPerDoctor; i++) {
//                LocalDate date = startDate.plusDays(i);
//                // Example: 9AM to 5PM, 1-hour slots
//                for (int hour = 9; hour < 17; hour++) {
//                    DoctorAvailability availability = DoctorAvailability.builder()
//                            .doctor(doctor)
//                            .availableDate(date)
//                            .startTime(LocalTime.of(hour, 0))
//                            .endTime(LocalTime.of(hour + 1, 0))
//                            .isBooked(false)
//                            .build();
//                    doctorService.saveAvailability(availability);
//                }
//            }
//        });
//    }
//
//
//    private void seedPatients(int count) {
//        for (int i = 1; i <= count; i++) {
//            User user = User.builder()
//                    .userName("patient" + i)
//                    .userEmail("patient" + i + "@example.com")
//                    .userPassword(passwordEncoder.encode("Patient@123"))
//                    .role(UserRole.PATIENT)
//                    .createdAt(LocalDateTime.now())
//                    .updatedAt(LocalDateTime.now())
//                    .build();
//
//            Patient patient = Patient.builder()
//                    .user(user) // cascades automatically
//                    .firstName(faker.name().firstName())
//                    .lastName(faker.name().lastName())
//                    .dateOfBirth(faker.date().birthday().toInstant()
//                            .atZone(java.time.ZoneId.systemDefault())
//                            .toLocalDate())
//                    .gender(random.nextBoolean() ? Gender.MALE : Gender.FEMALE)
//                    .phoneNumber(faker.phoneNumber().cellPhone())
//                    .address(faker.address().fullAddress())
//                    .emergencyPhoneNumber(faker.phoneNumber().cellPhone())
//                    .createdAt(LocalDateTime.now())
//                    .updatedAt(LocalDateTime.now())
//                    .build();
//
//            patientService.savePatient(patient);
//        }
//    }
//}
//
//
//
//
