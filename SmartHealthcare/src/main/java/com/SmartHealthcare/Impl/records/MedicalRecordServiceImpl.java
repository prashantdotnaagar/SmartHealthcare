package com.SmartHealthcare.Impl.records;

import com.SmartHealthcare.dto.response.record.MedicalRecordRes;
import com.SmartHealthcare.model.patient.Patient;
import com.SmartHealthcare.model.records.MedicalRecord;
import com.SmartHealthcare.repository.patient.PatientRepository;
import com.SmartHealthcare.repository.records.MedicalRecordRepo;
import com.SmartHealthcare.service.records.MedicalRecordService;
import com.SmartHealthcare.util.record.RecordDTOConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Value("${medical.records.upload.dir}")
    private String uploadDir;

    @Autowired
    private  MedicalRecordRepo medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;


    @Override
    public MedicalRecord saveMedicalRecord(Long patientId, MultipartFile file, String notes) throws IOException {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.createDirectories(filePath.getParent());

        Files.write(filePath,file.getBytes(), StandardOpenOption.CREATE);

        MedicalRecord record = MedicalRecord.builder()
                .patient(patient)
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .filePath(filePath.toString())
                .uploadedAt(LocalDateTime.now())
                .notes(notes)
                .build();

        return medicalRecordRepository.save(record);
    }

    @Override
    public List<MedicalRecordRes> getMedicalRecordByPatient(Long patientId) {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientPatientId(patientId);
        return records.stream()
                .map(RecordDTOConverter::toDTO)
                .collect(Collectors.toList());
    }
}
