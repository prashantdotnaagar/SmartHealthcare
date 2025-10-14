package com.SmartHealthcare.controller.patient;


import com.SmartHealthcare.model.patient.Patient;
import com.SmartHealthcare.service.patient.PatientService;
import com.SmartHealthcare.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {


    @Autowired
    PatientService patientService;

    @Autowired
    UserService userService;

    @GetMapping("/all")
    ResponseEntity<List<Patient>>getAllPatient(){
        List<Patient> patients=patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }


    @DeleteMapping("/{id}")
    ResponseEntity deletePatient(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User Deleted Successfully");
    }
}
