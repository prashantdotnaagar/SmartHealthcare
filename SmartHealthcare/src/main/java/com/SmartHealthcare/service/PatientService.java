package com.SmartHealthcare.service;


import com.SmartHealthcare.Impl.PatientServiceImpl;
import com.SmartHealthcare.model.Patient;
import com.SmartHealthcare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientServiceImpl patientServiceimpl;

    public void savePatient(Patient patient){
        patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientServiceimpl.getAllPatients();
    }

}
