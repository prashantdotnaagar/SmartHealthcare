package com.SmartHealthcare.util.record;

import com.SmartHealthcare.dto.response.record.MedicalRecordRes;
import com.SmartHealthcare.model.records.MedicalRecord;

public class RecordDTOConverter {
    public static MedicalRecordRes toDTO(MedicalRecord record) {
        if (record == null) return null;

        return MedicalRecordRes.builder()
                .recordId(record.getRecordId())
                .fileName(record.getFileName())
                .filePath(record.getFilePath())
                .fileType(record.getFileType())
                .uploadedAt(record.getUploadedAt())
                .notes(record.getNotes())
                .patientId(record.getPatient().getPatientId())
                .patientName(record.getPatient().getFirstName() + " " + record.getPatient().getLastName())
                .build();
    }
}
