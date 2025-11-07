package com.SmartHealthcare.service.records;

import com.SmartHealthcare.dto.response.record.MedicalRecordRes;
import com.SmartHealthcare.model.records.MedicalRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MedicalRecordService {
    MedicalRecord saveMedicalRecord(Long patientId, MultipartFile file, String notes) throws IOException;
    List<MedicalRecordRes> getMedicalRecordByPatient(Long patientId);

}
