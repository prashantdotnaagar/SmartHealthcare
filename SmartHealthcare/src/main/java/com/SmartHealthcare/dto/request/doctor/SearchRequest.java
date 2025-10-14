package com.SmartHealthcare.dto.request.doctor;

import lombok.Data;

@Data
public class SearchRequest {
    private String specialization;
    private String location;
    private String language;
    private Integer minExperience;
    private Integer maxExperience;
}
