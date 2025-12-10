package com.SmartHealthcare.repository.records;


import com.SmartHealthcare.model.records.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepo extends JpaRepository<MedicalRecord,Long> {
    List<MedicalRecord> findByPatientPatientId(Long patientId);
}
