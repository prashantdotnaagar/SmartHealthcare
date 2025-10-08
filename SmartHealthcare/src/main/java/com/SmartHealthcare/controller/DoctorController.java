package com.SmartHealthcare.controller;

import com.SmartHealthcare.dto.request.doctor.FilterRequest;
import com.SmartHealthcare.dto.request.doctor.SearchRequest;
import com.SmartHealthcare.dto.response.doctor.DoctorByIdResDTO;
import com.SmartHealthcare.model.Doctor;
import com.SmartHealthcare.service.DoctorService;
import com.SmartHealthcare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    // Get all doctors
    @GetMapping("/all")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(doctors);
        }
        return ResponseEntity.ok(doctors);
    }

    // Get doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorByIdResDTO> getDoctorById(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.getDoctorById(id);
            // Using DTO to return only required info
            DoctorByIdResDTO dto = new DoctorByIdResDTO();
            dto.setUser_id(doctor.getDoctorId());
            dto.setFirstName(doctor.getFirstName());
            dto.setLastName(doctor.getLastName());
            dto.setLicenseNumber(doctor.getLicenseNumber());
            dto.setPhone(doctor.getPhone());
            dto.setSpecialization(doctor.getSpecialization());

            return ResponseEntity.ok(dto);

        } catch (RuntimeException e) {
            // Doctor not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Other server errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Delete doctor
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(@RequestBody SearchRequest request) {
        List<Doctor> result = doctorService.searchDoctors(
                request.getSpecialization(),
                request.getLocation(),
                request.getLanguage(),
                request.getMinExperience(),
                request.getMaxExperience()
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Doctor>> filterDoctors(@RequestBody FilterRequest filterRequest) {
        List<Doctor> doctors = doctorService.filterDoctors(filterRequest);
        return ResponseEntity.ok(doctors);
    }


}
