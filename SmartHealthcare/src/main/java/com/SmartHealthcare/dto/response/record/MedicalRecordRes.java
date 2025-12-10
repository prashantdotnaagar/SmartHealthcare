package com.SmartHealthcare.dto.response.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @   Builder
    public class MedicalRecordRes {
        private Long recordId;
        private String fileName;
        private String filePath;
        private String fileType;
        private LocalDateTime uploadedAt;
        private String notes;
        private Long patientId;     // optionally include this if you want to show which patient it belongs to
        private String patientName; // optional convenience field

}
