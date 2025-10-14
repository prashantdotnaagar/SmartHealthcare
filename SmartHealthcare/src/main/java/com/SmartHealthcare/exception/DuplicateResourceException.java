package com.SmartHealthcare.exception;

import com.SmartHealthcare.constants.ServiceCodes;

public class DuplicateResourceException extends ServiceException {
    public DuplicateResourceException(String message) {
        super(ServiceCodes.DUPLICATE_PATIENT, message);
    }

    public DuplicateResourceException(ServiceCodes serviceCode) {
        super(serviceCode);
    }
}
