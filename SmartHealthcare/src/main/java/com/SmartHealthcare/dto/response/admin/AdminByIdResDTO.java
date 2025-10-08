package com.SmartHealthcare.dto.response.admin;

import lombok.Data;

@Data
public class AdminByIdResDTO {
        private long adminId;
        private String firstName;
        private String lastName;
        private String department;
        private Integer accessLevel;

}
