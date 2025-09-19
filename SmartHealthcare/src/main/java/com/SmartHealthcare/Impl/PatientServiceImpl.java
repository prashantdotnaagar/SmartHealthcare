package com.SmartHealthcare.Impl;


import com.SmartHealthcare.model.Patient;
import com.SmartHealthcare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl {
    @Autowired
    private PatientRepository patientRepository;


    public List<Patient>getAllPatients(){
        return patientRepository.findAll();
    }
}
