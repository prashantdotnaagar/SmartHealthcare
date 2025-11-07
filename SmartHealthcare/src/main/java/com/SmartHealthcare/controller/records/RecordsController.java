package com.SmartHealthcare.controller.records;


import com.SmartHealthcare.dto.response.record.MedicalRecordRes;
import com.SmartHealthcare.model.records.MedicalRecord;
import com.SmartHealthcare.service.records.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/medical-records")
public class RecordsController {

    @Autowired
    private  MedicalRecordService medicalRecordService;

    @PostMapping("/upload/{patientId}")
    public ResponseEntity<String> uploadMedicalRecord(
            @PathVariable Long patientId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "notes", required = false) String notes
    ) throws IOException {

        medicalRecordService.saveMedicalRecord(patientId, file, notes);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordRes>> getMedicalRecords(@PathVariable Long patientId) {
        List<MedicalRecordRes> records = medicalRecordService.getMedicalRecordByPatient(patientId);
        return ResponseEntity.ok(records);
    }

}
