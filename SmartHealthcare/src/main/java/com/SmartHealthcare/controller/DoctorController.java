package com.SmartHealthcare.controller;


import com.SmartHealthcare.model.Doctor;
import com.SmartHealthcare.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @GetMapping("/all")
    ResponseEntity<List<Doctor>>getAllDoctor(){
        List<Doctor>doctors=doctorService.getAllDoctor();
        return ResponseEntity.ok(doctors);
    }
}
