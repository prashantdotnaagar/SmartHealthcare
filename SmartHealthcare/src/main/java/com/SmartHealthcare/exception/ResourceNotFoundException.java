package com.SmartHealthcare.exception;

import com.SmartHealthcare.constants.ServiceCodes;

public class ResourceNotFoundException extends ServiceException {
    public ResourceNotFoundException(ServiceCodes doctorNotFound, String message) {
        super(ServiceCodes.DOCTOR_NOT_FOUND, message);
    }

    public ResourceNotFoundException(ServiceCodes serviceCode) {
        super(serviceCode);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(ServiceCodes.DOCTOR_NOT_FOUND,
                String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}