package com.SmartHealthcare.controller;


import com.SmartHealthcare.model.Patient;
import com.SmartHealthcare.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {


    @Autowired
    PatientService patientService;

    @GetMapping("/all")
    ResponseEntity<List<Patient>>getAllPatient(){
        List<Patient> patients=patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }
}
