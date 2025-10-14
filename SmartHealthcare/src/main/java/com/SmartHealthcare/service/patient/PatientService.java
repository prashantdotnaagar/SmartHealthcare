package com.SmartHealthcare.service.patient;

import com.SmartHealthcare.model.patient.Patient;
import java.util.List;

public interface PatientService {

    void savePatient(Patient patient);

    List<Patient> getAllPatients();
}
