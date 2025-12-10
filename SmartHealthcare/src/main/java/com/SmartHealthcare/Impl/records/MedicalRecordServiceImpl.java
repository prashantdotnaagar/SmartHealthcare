package com.SmartHealthcare.Impl.records;

import com.SmartHealthcare.constants.ServiceCodes;
import com.SmartHealthcare.dto.response.record.MedicalRecordRes;
import com.SmartHealthcare.exception.ResourceNotFoundException;
import com.SmartHealthcare.exception.ServiceException;
import com.SmartHealthcare.model.patient.Patient;
import com.SmartHealthcare.model.records.MedicalRecord;
import com.SmartHealthcare.repository.patient.PatientRepository;
import com.SmartHealthcare.repository.records.MedicalRecordRepo;
import com.SmartHealthcare.service.records.MedicalRecordService;
import com.SmartHealthcare.util.record.RecordDTOConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Value("${medical.records.upload.dir}")
    private String uploadDir;

    @Autowired
    private MedicalRecordRepo medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MedicalRecord saveMedicalRecord(Long patientId, MultipartFile file, String notes) throws IOException {
        try {
            log.info("Saving medical record for patient ID: {}", patientId);

            validateSaveRequest(patientId, file);

            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> {
                        log.warn("Patient not found with ID: {}", patientId);
                        return new ResourceNotFoundException(ServiceCodes.PATIENT_NOT_FOUND);
                    });

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            try {
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE);
                log.info("File saved successfully at: {}", filePath);
            } catch (IOException e) {
                log.error("Failed to save file to disk: {}", e.getMessage(), e);
                throw new ServiceException(ServiceCodes.SAVE_FAILED);
            }

            MedicalRecord record = MedicalRecord.builder()
                    .patient(patient)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .filePath(filePath.toString())
                    .uploadedAt(LocalDateTime.now())
                    .notes(notes)
                    .build();

            MedicalRecord savedRecord = medicalRecordRepository.save(record);
            log.info("Successfully saved medical record with ID: {}", savedRecord.getRecordId());
            return savedRecord;

        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while saving medical record: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while saving medical record: {}", e.getMessage(), e);
            throw new ServiceException(ServiceCodes.SAVE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordRes> getMedicalRecordByPatient(Long patientId) {
        try {
            log.info("Fetching medical records for patient ID: {}", patientId);

            if (patientId == null || patientId <= 0) {
                log.warn("Invalid patient ID provided: {}", patientId);
                throw new IllegalArgumentException("Patient ID must be a positive number");
            }

            List<MedicalRecord> records = medicalRecordRepository.findByPatientPatientId(patientId);
            log.info("Retrieved {} medical records for patient ID: {}", records.size(), patientId);

            return records.stream()
                    .map(RecordDTOConverter::toDTO)
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while fetching medical records for patient ID {}: {}",
                    patientId, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.DATABASE_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while fetching medical records for patient ID {}: {}",
                    patientId, e.getMessage(), e);
            throw new ServiceException(ServiceCodes.INTERNAL_ERROR);
        }
    }

    private void validateSaveRequest(Long patientId, MultipartFile file) {
        if (patientId == null || patientId <= 0) {
            log.warn("Invalid patient ID: {}", patientId);
            throw new IllegalArgumentException("Patient ID must be a positive number");
        }
        if (file == null || file.isEmpty()) {
            log.warn("File is null or empty");
            throw new IllegalArgumentException("File cannot be null or empty");
        }
        if (file.getOriginalFilename() == null || file.getOriginalFilename().trim().isEmpty()) {
            log.warn("File name is missing");
            throw new IllegalArgumentException("File must have a valid name");
        }
    }
}