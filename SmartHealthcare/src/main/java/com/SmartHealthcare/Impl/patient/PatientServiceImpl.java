package com.SmartHealthcare.Impl.patient;

import com.SmartHealthcare.constants.ServiceCodes;
import com.SmartHealthcare.exception.ServiceException;
import com.SmartHealthcare.model.patient.Patient;
import com.SmartHealthcare.repository.patient.PatientRepository;
import com.SmartHealthcare.service.patient.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePatient(Patient patient) {
        try {
            log.info("Saving patient");
            patientRepository.save(patient);
            log.info("Successfully saved patient");
        } catch (DataAccessException e) {
            log.error("Database error while saving patient: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while saving patient: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.PATIENT_SAVE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        try {
            log.info("Fetching all patients");
            List<Patient> patients = patientRepository.findAll();
            log.info("Retrieved {} patients", patients.size());
            return patients;
        } catch (DataAccessException e) {
            log.error("Database error while fetching all patients: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while fetching all patients: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR);
        }
    }
}