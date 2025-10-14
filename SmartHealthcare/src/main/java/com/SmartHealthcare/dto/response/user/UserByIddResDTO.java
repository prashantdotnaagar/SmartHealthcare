package com.SmartHealthcare.dto.response.user;


import com.SmartHealthcare.constants.UserRole;
import lombok.Data;

@Data
public class UserByIddResDTO {
    private long     userId;
    private String   userName;
    private String   userEmail;
    private UserRole      Role;
}


